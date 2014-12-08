//Check and see what content to load in the home page
//If they are new user -- signup
//Registered user -- survey
var cName = 'uid';
var uid;
var loadHomePageContent = function(){
    if( $.cookie(cName) != "undefined")
        uid = $.cookie(cName); //Get user id from cookie
    var qs = $.qs('uid'); //OR get from URL
    if( qs !== null)
        uid = qs;
    
    //Store the user id as a cookie
    $.cookie(cName, uid, { expires: 1000, path: '/' });
    
    if( typeof(uid) !== "undefined")
    {
        //User has been here before so load survey
        $('#home-container').load('./subpages/survey.html', function(){
            $(".happyness").click(function(){
                var happyValue = $(this).data("happyvalue");
                //
                //SUBMIT HAPPYNESS VALUE TO THE CLOUD
                //
                var data = {"happiness": happyValue};
                $.ajax({
                    url: '/api/user/happiness?t='+uid,
                    type: "POST",
                    data: JSON.stringify(data),
                    contentType: "application/json",
                    accepts: 'application/json',
                    success: function(response) {
                      alert("Success");
                 }
                });
                window.location = "chart.html";
            });
        });
    }
    else
    {
        $('#home-container').load('./subpages/signup.html', function(){
            $('#mobile-number').mask("(999)999-9999");
            $('#sign-up').click(function (e) {
                var number = $('#mobile-number').val();
                //TO DO 
                //Add phone number validation
                //
                number = validateAndTrimPhoneNumber(number);
                if( number == -1){
                    $("#invalid-number").show();
                    return;
                }
                // 
                // TEXT MESSAGE CODE GOES HERE
                //
                
                $('#home-container').load('./subpages/messageSent.html');
            });
        });
    }
}
$(document).ready(function(){
    loadHomePageContent();
});

var validateAndTrimPhoneNumber = function(number)
{
    number = number.replace(/\(/g, '');
    number = number.replace(/\)/g, '');
    number = number.replace(/\-/g, '');
    number = number.replace(/ /g, '');
    if( $.isNumeric(number) === false || number.length != 10)
        return -1
    else
        return number;
}

