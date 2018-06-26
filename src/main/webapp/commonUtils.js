export function fillInTable(url, table){
    table.empty();
    table.append('' +
        '<tr><th>Amount in</th>' +
        '<th>Currency in</th>' +
        '<th>Exchange rate</th>' +
        '<th>Amount out</th>' +
        '<th>Currency out</th>' +
        '<th>DATE</th></tr>');
    $.getJSON(url, function (data) {
        $.each(data, function (key, entry) {
            table.append('' +
                '<tr><td>'+entry.amountIn+'</td>' +
                '<td>'+entry.currencyIn+'</td>' +
                '<td> --> ' + (entry.amountOut/entry.amountIn).toFixed(4) + ' --> </td>' +
                '<td>'+entry.amountOut.toFixed(4)+'</td>' +
                '<td>'+entry.currencyOut+'</td>' +
                '<td>'+entry.date.dayOfMonth+'/' + entry.date.month +
                '/' + entry.date.year +' '+ entry.date.hour + ':' +
                entry.date.minute + ':' + entry.date.second + '</td></tr>');
        })
    });
}