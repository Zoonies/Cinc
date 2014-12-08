//Load Config
$.getScript( "./js/ChartGlobalConfig.js");



$(document).ready(function () {
    
    getDataStreams()
    .done(function(dataStreamReponse){
        var datasets = [];
        for(var i in dataStreamReponse)
        {
             datasets.push(dataStreamReponse[i].id);
        }
        var healthChartConfig = {
            "days" : 7,
            "datasets" : datasets
        }
        var healthData = {
            "setsStillLoading": datasets.length
        }
        var colors = [];
        colors[0] = ["rgba(52,52,119,0.2)", "rgba(52,52,119,1)", "rgba(128, 128, 179,1)", "rgba(220,220,220,1)" ];
        colors[1] = ["rgba(170,121,57,0.2)", "rgba(170,121,57,1)", "rgba(212, 167, 106,1)", "rgba(151,187,205,1)" ];
        colors[2] = ["rgba(170,160,57,0.2)", "rgba(170,160,57,1)", "rgba(212, 203, 106,1)", "rgba(151,187,205,1)" ];
        
        var linesStyles = {};
        var iterator = 0;
        for(var s in datasets)
        {   
            var style = {
                label: s,
                fillColor: colors[i][0],
                strokeColor: colors[i][1],
                pointColor: colors[i][2],
                pointStrokeColor: "#fff",
                pointHighlightFill: "#fff",
                pointHighlightStroke: colors[i][3]
            }
            linesStyles[s] = style;
            iterator ++;
        }
        linesStyles["default"] = {
            label: "happiness",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)"
        };
      
        //TODO iterate over each stream to set data
        pullDataSets();

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
        // Because the ajax pulls are async we'll want to call this a different way (once they are all done loading)
        //populateInitialChart(); //Moved to "setDoneLoading"
        //
    })
    .fail(function(x) {
        alert("Failed to load data streams from server!");
    })
    

    function getDataStreams() {
        var data = {"": 0};
        return $.ajax({
            beforeSend: function(xhrObj){
                xhrObj.setRequestHeader("Content-Type","application/json");
                xhrObj.setRequestHeader("Accept","application/json");
            },
            url: '/api/streams',
            type: "GET",
            dataType: 'json'
        });
    }
});



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

var parseDateString = function(fullSet)
{
    //Recieving from the server: 2014-12-06T08:00:00.000Z
    //Parsing into client format: 12/6/2014
    for( var i=0; i< fullSet.length; i++)
    {
        var serverDate = fullSet[i][0]; //This assumes the date is the first item.
        var temp = new Date(serverDate);
        var clientDate =  dateToString(temp);
        fullSet[i][0] = clientDate;
    }
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


// Because the ajax pulls are async we wait tillthey are all done loading to draw the chart
var setDoneLoading = function(setName)
{
    healthData.setsStillLoading--;
    if ( healthData.setsStillLoading === 0)
        populateInitialChart();
}

var pullDataSets = function()
{
    
    var happinessRawData = {
        "11/14/2014" : 1,
        "11/15/2014" : 4
    };
    var pollenRawData;
    // $.ajax({
    //     url: '/api/streams/pollen',
    //     type: "GET",
    //     contentType: "application/text",
    //     accepts: 'application/json',
    //     success: function(response) {
    //         healthData.pollen = parseDateString(response);
    //         setDoneLoading("pollen");
    //     },
    //      error: function(response) {
    //         setDoneLoading("pollen");
    //      }
    // });
    
    
    var crimeRawData;
    
    //healthData.happiness = happinessRawData; 
    //setDoneLoading("happiness");
   // healthData.pollen =  pollenRawData;
    healthData.crime =  crimeRawData;
    setDoneLoading("crime");
}