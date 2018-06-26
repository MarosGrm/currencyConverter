import {fillInTable} from "./commonUtils.js";

$(document).ready(function() {
    let fromDropdown = $('#fromCurrency');
    let toDropdown = $('#toCurrency');
    fillUpSelect(fromDropdown);
    fillUpSelect(toDropdown);
    fillInTable('/GetStartedJava/api/history/last/10', $('#results'));
    setUpForm($("#postForm"));
});

$.validator.addMethod('positiveNumber',
    function (value) {
        return Number(value) > 0;
    }, 'Enter a positive number.');

function setUpForm(formSetUp){
    formSetUp.validate({
        rules: {
            amountIn: {
                required: true,
                number : true,
                positiveNumber: true
            },
            currencyIn: {
                required: true,
            },
            currencyOut: {
                required: true,
            }
        },
        submitHandler: function(form){
            event.preventDefault(); //prevent default action
            var data = $("#postForm").serializeArray(); //Encode form elements for submission
            var object  = {};
            for (var i = 0; i < data.length-1; i++) { //TODO -1, hack for SUBMIT
                object[data[i].name] = data[i].value;
            }

            var dataToPost = JSON.stringify(object);
            console.log(dataToPost);

            $.ajax({
                headers: {
                    'Content-Type': 'application/json'
                },
                url : '/GetStartedJava/api/currency/convert',
                type: 'POST',
                dataType: 'json',
                data : dataToPost
            }).done(function(response){
                $("#OutputValue").text('Output amount is ' + response.amountOut);
                fillInTable('/GetStartedJava/api/history/last/10', $('#results'));
            });
        }
    });
}

function fillUpSelect(dropdown){
    dropdown.empty();

    dropdown.append('<option selected="true" disabled>Choose Currency</option>');
    dropdown.prop('selectedIndex', 0);
    console.log(dropdown);
    const url = '/GetStartedJava/api/currency/available';

    // Populate dropdown with list of provinces
    $.getJSON(url, function (data) {
        data.sort(function(a, b){
            if(a.name>b.name){
                return 1;
            }
            else if (a.name===b.name){
                return 0;
            }
            else {
                return -1;
            }
        });
        $.each(data, function (key, entry) {
            dropdown.append($('<option></option>').attr('value', entry.code).text(entry.name));
        })
    });

    dropdown.on('change', function() {
        let info = $('#exchangeRate');
        info.empty();
        let from = $("#fromCurrency").val();
        let to = $("#toCurrency").val();
        const url = '/GetStartedJava/api/currency/rate/' + from + '/' + to;
        $.getJSON(url, function (data) {
            if(data === -1){
                info.append('<p>Exchange rate is: N/A</p>');
            }else{
                info.append('<p>Exchange rate ' + from + ' -> ' + to + ' is: <b>' + data.toFixed(4) +'</b></p>');
            }
        });
    })
}