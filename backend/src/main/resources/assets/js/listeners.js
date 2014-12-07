//Clears all childen of "className"
//Adds active to "newActive" element
var clearActive = function(className, newActive)
{
    $("."+className + " .active").removeClass("active").addClass("btn-default"); 
    $(newActive).addClass("active").removeClass("btn-default"); 
}

//Toggles Active highlight
var toggleActive = function(element)
{
    $(element).toggleClass("active").toggleClass("btn-default"); 
}

//On click for the range selection
$(".range").click(function (e) {
    var days = $(this).data("range"); //Days
    if(healthChartConfig.days != days) //Don't update if there is no change
    {
        clearActive("range-group", this); //Update UI
        healthChartConfig.days = days;
        updateChart(); //Update chart
    }
});

//Set initiially active elements
$(".dataset").each(function (index) {
    var name = $(this).data("set-name"); //Get the name
    if( $.inArray(name, healthChartConfig.datasets) > -1) //Check if it's in the array
    {
        toggleActive (this); // toggle to active
        var color = linesStyles[name].fillColor;
        $(this).css("background-color", color);
    }
});

//On click when user changes the datasets they are viewing.
$(".dataset").click(function (e) {
     var name = $(this).data("set-name"); //Get the name
     
     if( $.inArray(name, healthChartConfig.datasets) > -1)
     {
         //Disable
         healthChartConfig.datasets.remove(name);
         updateChart();
          $(this).css("background-color", "");
     }
     else
     {
         //Enable
         healthChartConfig.datasets.push(name);
         updateChart();
         var color = linesStyles[name].fillColor;
         $(this).css("background-color", color);
     }
     toggleActive (this);
});
