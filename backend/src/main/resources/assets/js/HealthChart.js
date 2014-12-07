//Load Config
$.getScript( "./js/ChartGlobalConfig.js");

var healthChartConfig = {
    "days" : 7,
    "datasets" : ["happiness", "pollen"]
}
var healthData = {
    //"happiness": [2,3,5,3.etc],
    //"pollen": [34.23,43,etc]
}
var linesStyles = {
    "happiness": {
        label: "happiness",
        fillColor: "rgba(52,52,119,0.2)",
        strokeColor: "rgba(52,52,119,1)",
        pointColor: "rgba(128, 128, 179,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)"
    },
    "pollen": {
        label: "pollen",
        fillColor: "rgba(170,121,57,0.2)",
        strokeColor: "rgba(170,121,57,1)",
        pointColor: "rgba(212, 167, 106,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(151,187,205,1)"
    },
    "crime": {
        label: "pollen",
        fillColor: "rgba(170,160,57,0.2)",
        strokeColor: "rgba(170,160,57,1)",
        pointColor: "rgba(212, 203, 106,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(151,187,205,1)"
    },
    "default": {
        label: "happiness",
        fillColor: "rgba(220,220,220,0.2)",
        strokeColor: "rgba(220,220,220,1)",
        pointColor: "rgba(220,220,220,1)",
        pointStrokeColor: "#fff",
        pointHighlightFill: "#fff",
        pointHighlightStroke: "rgba(220,220,220,1)"
    }
}

//Gets the line style based on the set name
var getStyle = function(setName)
{
    var style = linesStyles[setName];
    if( typeof(style) == "undefined")
         style = linesStyles["default"];
    
    return style;
}

//Get the datapoint in "setName" 
//If no value is present an empty string is sent so ChartJS doesn't error out
var getDataPoint = function(setName, date)
{
    var set = healthData[setName];
    if(typeof(set) == "undefined")
        return "";
    var point = set[date];
    if(typeof(point) == "undefined")
        return "";
    else
        return point;
}



var getChartData = function()
{
    //Number of days to display
    var numberOfHistoricalDays = healthChartConfig.days;
    
    //Date
    var today = new Date();
    var labels = [ ];
    
    //Final that is passed to CharJS
    var data = {};
    data.datasets = [];
    
    //Loop through for each dataset and pull out subset based on the given range.
    for(var a = 0;  a < healthChartConfig.datasets.length; a++)
    {
        var cData = []; //Subset
        var setName = healthChartConfig.datasets[a]; //Name of set
        for (var i=numberOfHistoricalDays; i >= 1 ; i-- )
        {
            var current = new Date();
            current.setDate(today.getDate()-i);
            var dateString =  dateToString(current); //Create the data string
            
            
            var dataPoint = getDataPoint(setName, dateString); //Get the data point
            cData.push(dataPoint); //Add to subset
            
            if(a === 0) //Only capture on first loop to prevent duplicates
                labels.push(dateString); //Add to the X-Axis
        }
        
        data.datasets.push({});
        data.datasets[a].data = cData; //Add the data subset to the ChartJS object
        $.extend(data.datasets[a], getStyle(setName)); //Add the line style 
        
    }
    
    data.labels = labels; //Set the X-Axis
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
    pullDataSets();
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
    // CALL ON DATA PULL COMPLETE (FOR ALL SETS)
    populateInitialChart();
    //
});

var pullDataSets = function()
{
    //Pull the full range of data from the server.
    //This is can be filtered by the user on the client side.
    
   //Test-Fake-Sample Data
   // 
   //      $.ajax({
   //          url: '/api/streams/happiness',
   //          type: "GET",
   //          contentType: "application/json",
   //          accepts: 'application/json',
   //          success: function(response) {
   //            var happinessRawData = response;
   //       }
   //      });
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
        "12/6/2014" : 4
    };
    var pollenRawData;
    $.ajax({
        url: '/api/streams/pollen',
        type: "GET",
        contentType: "application/text",
        accepts: 'application/json',
        success: function(response) {
            pollenRawData = response;
     }
    });
    
    // var pollenRawData = {
    //     "11/14/2014" : 41,
    //     "11/15/2014" : 54,
    //     "11/16/2014" : 43,
    //     "11/17/2014" : 21,
    //     "11/18/2014" : 31,
    //     "11/19/2014" : 25,
    //     "11/20/2014" : 14,
    //     "11/21/2014" : 34,
    //     "11/22/2014" : 24,
    //     "11/23/2014" : 73,
    //     "11/24/2014" : 41,
    //     "11/25/2014" : 41,
    //     "11/26/2014" : 35,
    //     "11/27/2014" : 34,
    //     "11/28/2014" : 51,
    //     "11/29/2014" : 42,
    //     "11/30/2014" : 44,
    //     "12/1/2014" : 32,
    //     "12/2/2014" : 25,
    //     "12/3/2014" : 55,
    //     "12/4/2014" : 21,
    //     "12/5/2014" : 43,
    //     "12/6/2014" : 25
    // };
    var crimeRawData = {
        "11/14/2014" : 45,
        "11/15/2014" : 23,
        "11/16/2014" : 64,
        "11/17/2014" : 45,
        "11/18/2014" : 34,
        "11/19/2014" : 23,
        "11/20/2014" : 23,
        "11/21/2014" : 54,
        "11/22/2014" : 45,
        "11/23/2014" : 45,
        "11/24/2014" : 63,
        "11/25/2014" : 35,
        "11/26/2014" : 34,
        "11/27/2014" : 22,
        "11/28/2014" : 34,
        "11/29/2014" : 42,
        "11/30/2014" : 23,
        "12/1/2014" : 23,
        "12/2/2014" : 12,
        "12/3/2014" : 43,
        "12/4/2014" : 53,
        "12/5/2014" : 11,
        "12/6/2014" : 11
    };
    
    healthData.happiness = happinessRawData;
    healthData.pollen =  pollenRawData;
    healthData.crime =  crimeRawData;
}