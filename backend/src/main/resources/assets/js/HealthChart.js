//Load Config
$.getScript( "js/ChartGlobalConfig.js");
$.getScript( "js/helpers.js");
    //Test Data Set
    var happinessRawData = {
        "11/14/2014" : 1,
        "11/15/2014" : 4,
        "11/16/2014" : 3,
        "11/17/2014" : 1,
        "11/18/2014" : 1,
        "11/19/2014" : 5,
        "11/20/2014" : 4,
        "11/21/2014" : 1,
        "11/22/2014" : 4,
        "11/23/2014" : 3,
        "11/24/2014" : 1,
        "11/25/2014" : 1,
        "11/26/2014" : 5,
        "11/27/2014" : 4,
        "11/28/2014" : 1,
        "11/29/2014" : 2,
        "11/30/2014" : 4,
        "12/1/2014" : 2,
        "12/2/2014" : 5,
        "12/3/2014" : "",
        "12/4/2014" : 1,
        "12/5/2014" : 2,
        "12/5/2014" : 4
    };
    var pollenRawData = {
        "11/14/2014" : 41,
        "11/15/2014" : 54,
        "11/16/2014" : 43,
        "11/17/2014" : 21,
        "11/18/2014" : 31,
        "11/19/2014" : 25,
        "11/20/2014" : 14,
        "11/21/2014" : 34,
        "11/22/2014" : 24,
        "11/23/2014" : 73,
        "11/24/2014" : 41,
        "11/25/2014" : 41,
        "11/26/2014" : 35,
        "11/27/2014" : 34,
        "11/28/2014" : 51,
        "11/29/2014" : 42,
        "11/30/2014" : 44,
        "12/1/2014" : 32,
        "12/2/2014" : 25,
        "12/3/2014" : 55,
        "12/4/2014" : 21,
        "12/5/2014" : 43,
        "12/5/2014" : 25
    };

var healthChartConfig = {
    "days" : 7,
    "datasets" : ["happiness", "pollen"]
}



var getChartData = function()
{

    //Settings
    var numberOfHistoricalDays = healthChartConfig.days;
    
    //Prep datasets
    var happinessData=[];
    var pollenData=[];
    //Date
    var today = new Date();
    var labels = [ ];
    for (var i=numberOfHistoricalDays; i >= 1 ; i-- )
    {
        var current = new Date();
        current.setDate(today.getDate()-i);
        var dateString =  dateToString(current);
        labels.push(dateString);
        var happyDayValue =  happinessRawData[dateString];
        var pollenDayValue =  pollenRawData[dateString];
        happinessData.push(happyDayValue);
        pollenData.push(pollenDayValue+5);
    }

    //Example dataset
    var data = {
        labels: labels,
        datasets: [
            {
                label: "My First dataset",
                fillColor: "rgba(220,220,220,0.2)",
                strokeColor: "rgba(220,220,220,1)",
                pointColor: "rgba(220,220,220,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(220,220,220,1)",
                data: happinessData
            },
            {
                label: "My Second dataset",
                fillColor: "rgba(151,187,205,0.2)",
                strokeColor: "rgba(151,187,205,1)",
                pointColor: "rgba(151,187,205,1)",
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: "rgba(151,187,205,1)",
                data: pollenData
            }
        ]
    };
    return data;
}

var updateChart = function()
{
    //Pull new data
    var data = getChartData();
    
    //Destory old Chart
    //healthChartConfig.ctx.clearRect(0,0,healthChartConfig.canvas.width, healthChartConfig.canvasheight);
    healthChartConfig.myChart.destroy();
   
  //create new Chart
    var options = Chart.defaults.global;
    healthChartConfig.myChart = new Chart(healthChartConfig.ctx).Line(data, options);
}

var populateInitialChart = function()
{
    var data = getChartData();
    var options = Chart.defaults.global;
    healthChartConfig.myChart= new Chart(healthChartConfig.ctx).Line(data, options);
}

$(document).ready(function () {
    //
    // PULL DATA FROM SERVER
    //
    
    // Get context with jQuery - using jQuery's .get() method.
    var ctx = $("#myChart").get(0).getContext("2d");
    var canvas = $("#myChart").get(0);
    // This will get the first returned node in the jQuery collection.
    var myNewChart = new Chart(ctx);
    
    healthChartConfig.ctx =  ctx;
    healthChartConfig.myChart = myNewChart;
    healthChartConfig.canvas = canvas;
    
    //
    // ON DATA PULL COMPLETE
    //
    populateInitialChart();

});