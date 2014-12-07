//Clears all childen of "className"
//Adds active to "newActive" element
var clearActive = function(className, newActive)
{
    $("."+className + " .active").removeClass("active").addClass("btn-default"); 
    $(newActive).addClass("active").removeClass("btn-default"); 
}

var toggleActive = function(element)
{
    $(element).toggleClass("active").toggleClass("btn-default"); 
}

$(".range").click(function (e) {
    var days = $(this).data("range");
    if(healthChartConfig.days != days)
    {
        clearActive("range-group", this);
        healthChartConfig.days = days;
        updateChart();
    }
});

$(".dataset").click(function (e) {
     var name = $(this).data("set-name");
     
     if( $.inArray(name, healthChartConfig.datasets) > -1)
     {
         //Disable
         healthChartConfig.datasets.remove(name);
         updateChart();
     }
     else
     {
         //Enable
         healthChartConfig.datasets.push(name);
         updateChart;
     }
     toggleActive (this);
});
