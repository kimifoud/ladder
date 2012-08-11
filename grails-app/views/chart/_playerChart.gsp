<div id="chart"></div>
<gvisualization:lineCoreChart dynamicLoading="${true}" elementId="chart" title="Rank and rating over time"
                              width="${width ?: 600}" curveType="function" height="${300}" columns="${columns}"
                              data="${data}"
                              vAxes="${new Expando([0: new Expando(logScale: false), 1: new Expando(logScale: false, minValue: 1, direction: -1)])}"
                              legend="top"
                              series="${new Expando([0: new Expando(targetAxisIndex: 0), 1: new Expando(targetAxisIndex: 1)])}"/>