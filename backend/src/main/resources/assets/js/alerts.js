//Generate a fake alert on click.
$("#gen-alert").click(function(){
    var text = "Today has a higher than average pollen count and lower than average temperature. On days like this you normally don't feel well";
    text += " so take percautions.";
    createAlert(text);
});


var createAlert = function(text)
{
    var html = "<div class='alert alert-info alert-dismissible' role='alert'>";
    html += "<button type='button' class='close' data-dismiss='alert'><span aria-hidden='true'>&times;</span><span class='sr-only'>Close</span></button>";
    html += "<strong>Alert!</strong> "+text+"</div>"
    
    $("#alerts").append(html);
}