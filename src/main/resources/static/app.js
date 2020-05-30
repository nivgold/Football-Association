var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/game/'+$("#gameID").val(), function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    $.ajax({
        type: "POST",
        url: "http://lvh.me:5001/api/v1/addEventToGame",
        data: JSON.stringify({refereeUsername: "main Referee asaf",
        gameID: 1,
        gameMinute: 20,
        description: 'Koren Ishlah blabla',
        type: 'Foul',
        playerUsername: 'teamOwner_Niv'}),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });
    // $.post("http://lvh.me:5001/api/v1/getLeagueNames", function () {
    //
    // });
    //stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});