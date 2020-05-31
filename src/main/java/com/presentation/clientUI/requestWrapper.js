function sendNoParamsGet(url) {
    return $.get(url, function(responseText) {
                let response =  responseText;
                return response;
            }).fail(function() {return null;});
}

function sendParamsGet(url,params) {
    return $.get(url+"?"+params, function(responseText) {
        return responseText;
    }).fail(function() {return null;});;
}

function sendPostReq(urlArg, jsonData) {
    let response = $.ajax({
        type: 'POST',
        url: urlArg,
        data: jsonData,
        success: function(response) { return response },
        error: function() { return null },
        contentType: "application/json",
        dataType: 'json'
    });
    return response;
}