//Check and see what content to load in the home page
//If they are new user -- signup
//Registered user -- survey

$(document).ready(function(){
    //TO DO: ADD COOKIE LOGIC
   $('#home-container').load('/subpages/survey.html', function(){
       
       
   });
    // $('#home-container').load('/subpages/signup.html', function(){
    //     $('#mobile-number').mask("(999) 999-9999");
    //     $('#sign-up').click(function (e) {
    //         var number = $('#mobile-number').val();
    //         console.log(number);
    //         // 
    //         // TEXT MESSAGE CODE GOES HERE
    //         //
            
    //         $('#home-container').load('/subpages/messageSent.html');
    //     });
    // });

});

