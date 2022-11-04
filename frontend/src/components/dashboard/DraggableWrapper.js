import produce from 'immer';
import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import counterpart from 'counterpart';

import { connectWS, disconnectWS } from '../../utils/websockets';
import {
  addDashboardWidget,
  changeKPIItem,
  changeTargetIndicatorsItem,
  getKPIsDashboard,
  getTargetIndicatorsDashboard,
  removeDashboardWidget,
} from '../../actions/DashboardActions';
import logo from '../../assets/images/metasfresh_logo_green_thumb.png';
import RawChart from '../charts/RawChart';
import RawList from '../widget/List/RawList';
import ChartWidget from './ChartWidget';
import DndWidget from './DndWidget';
import Placeholder from './Placeholder';
import Sidenav from './Sidenav';
import EntityType from './EntityType';

export class DraggableWrapper extends Component {
  state = {
    cards: [],
    indicators: [],
    indicatorsLoaded: false,
    idMaximized: null,
    websocketEndpoint: null,
    chartOptions: false,
    captionHandler: '',
    when: '',
    interval: '',
    currentId: '',
    isIndicator: '',
    listFocused: null,
    listToggled: null,
  };

  componentDidMount = () => {
    this.loadKPIs();
    this.loadTargetIndicators();
  };

  componentDidUpdate = (prevProps, prevState) => {
    const { websocketEndpoint } = this.state;
    if (
      websocketEndpoint !== null &&
      prevState.websocketEndpoint !== websocketEndpoint
    ) {
      connectWS.call(this, websocketEndpoint, (msg) => {
        msg.events.map((event) => this.onWebsocketEvent(event));
      });
    }
  };

  componentWillUnmount = () => {
    disconnectWS.call(this);
  };

  handleFocus = (name) => {
    this.setState({
      listFocused: name,
    });
  };

  handleBlur = () => {
    this.setState({
      listFocused: null,
    });
  };

  closeDropdown = () => {
    this.setState({
      listToggled: null,
    });
  };

  openDropdown = (name) => {
    this.setState({
      listToggled: name,
    });
  };

  onWebsocketEvent = (event) => {
    switch (event.changeType) {
      case 'itemDataChanged':
        this.onDashboardItemDataChanged(event);
        break;
      case 'dashboardChanged':
      case 'itemChanged':
        this.onDashboardStructureChanged(event);
        break;
    }
  };

  onDashboardItemDataChanged = (event) => {
    const { indicators, cards } = this.state;

    const indicatorsNew = produce(indicators, (draft) => {
      const index = draft.findIndex(
        (indicator) => indicator.id === event.itemId
      );
      if (index !== -1) {
        draft[index] = { ...draft[index], data: event.data };
      }
    });

    const cardsNew = produce(cards, (draft) => {
      const index = cards.findIndex((card) => card.id === event.itemId);
      if (index !== -1) {
        draft[index] = { ...draft[index], data: event.data };
      }
    });

    this.setState({ cards: cardsNew, indicators: indicatorsNew });
  };

  onDashboardStructureChanged = (event) => {
    switch (event.widgetType) {
      case 'TargetIndicator':
        this.loadTargetIndicators();
        break;
      case 'KPI':
        this.loadKPIs();
        break;
    }
  };

  loadTargetIndicators = () => {
    getTargetIndicatorsDashboard().then((response) => {
      this.setState({
        indicators: response.data.items,
        indicatorsLoaded: true,
      });
    });
  };

  loadKPIs = () => {
    getKPIsDashboard().then((response) => {
      this.setState({
        cards: response.data.items,
        websocketEndpoint: response.data.websocketEndpoint,
      });
    });
  };

  getType = (entity) => {
    return entity === EntityType.KPI ? 'kpis' : 'targetIndicators';
  };

  onDrop = ({ entity, id, isNew, droppedOverId }) => {
    const position =
      this.state[entity]?.findIndex((item) => item.id === droppedOverId) ?? -1;

    //console.log('onDrop', { entity, id, isNew, droppedOverId, position });

    if (isNew) {
      addDashboardWidget(this.getType(entity), id, position);
      // NOTE: will be updated via websockets
    } else {
      if (entity === EntityType.KPI) {
        changeKPIItem(id, 'position', position);
        // NOTE: will be updated via websockets
      } else if (entity === EntityType.TARGET_INDICATOR) {
        changeTargetIndicatorsItem(id, 'position', position);
        // NOTE: will be updated via websockets
      } else {
        console.warn(`Unknown entity: ${entity}`);
      }
    }
  };

  onRemove = (entity, index, id) => {
    removeDashboardWidget(this.getType(entity), id);
    // NOTE: will be updated via websockets
  };

  maximizeWidget = (id) => {
    this.setState({ idMaximized: id });
  };

  renderIndicators = () => {
    const { indicators } = this.state;
    const { editmode } = this.props;

    if (!indicators.length && editmode)
      return (
        <div className="indicators-wrapper">
          <DndWidget
            id="indicatorsPlaceholder"
            index={-1}
            entity={EntityType.TARGET_INDICATOR}
            placeholder={true}
            transparent={false}
            onDrop={this.onDrop}
          >
            <Placeholder
              entity={EntityType.TARGET_INDICATOR}
              description={counterpart.translate(
                'dashboard.targetIndicators.dropContainer.caption'
              )}
            />
          </DndWidget>
        </div>
      );

    if (!indicators.length) return false;

    return (
      <div className={'indicators-wrapper'}>
        {indicators.map((indicator, index) => (
          <DndWidget
            key={indicator.id}
            id={indicator.id}
            index={index}
            entity={EntityType.TARGET_INDICATOR}
            transparent={!editmode}
            className="indicator-card"
            onDrop={this.onDrop}
            onRemove={this.onRemove}
          >
            {
              <RawChart
                key={indicator.id}
                id={indicator.id}
                index={index}
                caption={indicator.caption}
                fields={indicator.kpi.fields}
                zoomToDetailsAvailable={!!indicator.kpi.zoomToDetailsAvailable}
                chartType={'Indicator'}
                kpi={false}
                data={indicator.data}
                noData={indicator.fetchOnDrop}
                handleChartOptions={this.handleChartOptions}
                editmode={editmode}
              />
            }
          </DndWidget>
        ))}
      </div>
    );
  };

  renderEmptyPage = () => {
    const { editmode } = this.props;
    const { indicators, indicatorsLoaded } = this.state;

    // Don't show logo if edit mode is active
    if (editmode) return null;

    // Don't show logo if indicators/KPIs are not loaded yet
    if (!indicatorsLoaded) return null;

    // Don't show logo if we have indicators/KPIs
    if (indicators.length > 0) return null;

    return (
      <div className="dashboard-wrapper dashboard-logo-wrapper">
        <div className="logo-wrapper">
          <img src={logo} alt="logo" />
        </div>
      </div>
    );
  };

  renderKpis = () => {
    const { cards, idMaximized } = this.state;
    const { editmode } = this.props;

    if (!cards.length && editmode)
      return (
        <div className="kpis-wrapper">
          <DndWidget
            id="kpisPlaceholder"
            index={-1}
            placeholder={true}
            entity={EntityType.KPI}
            transparent={false}
            onDrop={this.onDrop}
          >
            <Placeholder
              entity={EntityType.KPI}
              description={counterpart.translate(
                'dashboard.kpis.dropContainer.caption'
              )}
            />
          </DndWidget>
        </div>
      );

    if (!cards.length) return false;

    return (
      <div className="kpis-wrapper">
        {cards.map((item, index) => {
          return (
            <DndWidget
              key={item.id}
              index={index}
              id={item.id}
              entity={EntityType.KPI}
              className={
                'draggable-widget ' +
                (idMaximized === item.id ? 'draggable-widget-maximize ' : '')
              }
              transparent={!editmode}
              onDrop={this.onDrop}
              onRemove={this.onRemove}
            >
              <ChartWidget
                key={item.id}
                id={item.id}
                index={index}
                chartType={item.kpi.chartType}
                caption={item.caption}
                fields={item.kpi.fields}
                groupBy={item.kpi.groupByField}
                idMaximized={idMaximized}
                maximizeWidget={this.maximizeWidget}
                text={item.caption}
                data={item.data}
                noData={item.fetchOnDrop}
                handleChartOptions={this.handleChartOptions}
                editmode={editmode}
              />
            </DndWidget>
          );
        })}
      </div>
    );
  };

  renderOptionModal = () => {
    const {
      chartOptions,
      captionHandler,
      when,
      interval,
      listFocused,
      listToggled,
    } = this.state;

    return (
      chartOptions && (
        <div className="chart-options-overlay">
          <div className="chart-options-wrapper">
            <div className="chart-options">
              <div className="form-group">
                <label>
                  {counterpart.translate('dashboard.item.settings.caption')}
                </label>
                <input
                  className="input-options input-secondary"
                  value={captionHandler}
                  onChange={this.handleChange}
                />
              </div>
              <div className="form-group">
                <label>
                  {counterpart.translate(
                    'dashboard.item.settings.interval.caption'
                  )}
                </label>
                <div className="chart-options-list-wrapper">
                  <RawList
                    onSelect={(option) =>
                      this.handleOptionSelect('interval', option)
                    }
                    tabIndex={0}
                    list={[
                      {
                        caption: counterpart.translate(
                          'dashboard.item.settings.interval.week'
                        ),
                        value: 'week',
                      },
                    ]}
                    selected={interval}
                    isFocused={listFocused === 'interval'}
                    isToggled={listToggled === 'interval'}
                    onFocus={() => this.handleFocus('interval')}
                    onBlur={() => this.handleBlur('interval')}
                    onOpenDropdown={() => this.openDropdown('interval')}
                    onCloseDropdown={() => this.closeDropdown('interval')}
                  />
                </div>
              </div>
              <div className="form-group">
                <label>
                  {counterpart.translate(
                    'dashboard.item.settings.when.caption'
                  )}
                </label>
                <div className="chart-options-list-wrapper">
                  <RawList
                    onSelect={(option) =>
                      this.handleOptionSelect('when', option)
                    }
                    list={[
                      {
                        caption: counterpart.translate(
                          'dashboard.item.settings.when.now'
                        ),
                        value: 'now',
                      },
                      {
                        caption: counterpart.translate(
                          'dashboard.item.settings.when.lastWeek'
                        ),
                        value: 'lastWeek',
                      },
                    ]}
                    tabIndex={0}
                    selected={when}
                    isFocused={listFocused === 'when'}
                    isToggled={listToggled === 'when'}
                    onFocus={() => this.handleFocus('when')}
                    onBlur={() => this.handleBlur('when')}
                    onOpenDropdown={() => this.openDropdown('when')}
                    onCloseDropdown={() => this.closeDropdown('when')}
                  />
                </div>
              </div>
            </div>
            <div className="chart-options-button-wrapper">
              <button
                className="btn btn-meta-outline-secondary btn-sm"
                onClick={() => this.changeChartData('caption', captionHandler)}
              >
                {counterpart.translate('dashboard.item.settings.save')}
              </button>
            </div>
          </div>
        </div>
      )
    );
  };

  handleChartOptions = (opened, caption, id, isIndicator) => {
    this.setState({
      chartOptions: opened,
      captionHandler: caption,
      currentId: id,
      when: opened ? this.state.when : '',
      interval: opened ? this.state.interval : '',
      isIndicator: !!isIndicator,
    });
  };

  handleOptionSelect = (path, option) => {
    const { currentId, isIndicator } = this.state;
    if (isIndicator) {
      changeTargetIndicatorsItem(currentId, path, option.value).then(() => {
        this.setSelectedOption(path, option);
      });
    } else {
      changeKPIItem(currentId, path, option.value).then(() => {
        this.setSelectedOption(path, option);
      });
    }
  };

  setSelectedOption = (path, option) => {
    const { when, interval } = this.state;
    this.setState({
      when: path === 'when' ? option : when,
      interval: path === 'interval' ? option : interval,
    });
  };

  handleChange = (e) => {
    this.setState({
      captionHandler: e.target.value,
    });
  };

  changeChartData = (path, value) => {
    const { currentId, isIndicator } = this.state;
    if (isIndicator) {
      changeTargetIndicatorsItem(currentId, path, value).then(() => {
        this.handleChartOptions(false);
      });
    } else {
      changeKPIItem(currentId, path, value).then(() => {
        this.handleChartOptions(false);
      });
    }
  };

  render() {
    const { editmode } = this.props;

    return (
      <DndProvider backend={HTML5Backend}>
        <div className="dashboard-cards-wrapper">
          {this.renderOptionModal()}
          <div className={editmode ? 'dashboard-edit-mode' : 'dashboard-cards'}>
            {this.renderIndicators()}
            {this.renderKpis()}
          </div>
          {editmode && <Sidenav />}
          {this.renderEmptyPage()}
        </div>
      </DndProvider>
    );
  }
}

DraggableWrapper.propTypes = {
  editmode: PropTypes.bool,
};

export default DraggableWrapper;
