function resetSystemReqJson(sysManagerUserName) {
    let json = "{sysManagerUserName: '" + sysManagerUserName + "'}";
    return json;
}

function teamRequestJson(ownerUserName, teamName, fieldCountry, fieldState, fieldCity, fieldPostalCode) {
    let obj = new Object();
    obj.ownerUserName = ownerUserName;
    obj.teamName = teamName;
    obj.fieldCountry = fieldCountry;
    obj.fieldState = fieldState;
    obj.fieldCity = fieldCity;
    obj.fieldPostalCode = fieldPostalCode;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function rankingPolicyRequestJson(associationAgentUsername, leagueName, seasonYear, win, goal, draw, yellowCards, redCards) {
    let obj = new Object();
    obj.associationAgentUsername = associationAgentUsername;
    obj.leagueName = leagueName;
    obj.seasonYear = seasonYear;
    obj.win = win;
    obj.goal = goal;
    obj.draw = draw;
    obj.yellowCards = yellowCards;
    obj.redCards = redCards;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function loginRequestJson(userName, password, firstName, lastName) {
    let obj = new Object();
    obj.userName = userName;
    obj.password = password;
    obj.firstName = firstName;
    obj.lastName = lastName;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function logoutRequestJson(username){
    let obj = new Object();
    obj.username = username;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function leagueRequestJson(leagueName) {
    let obj = new Object();
    obj.leagueName = leagueName;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function gameSettingPolicyRequestJson(associationAgentUsername, leagueName, seasonYear, gameSettingPolicy) {
    let obj = new Object();
    obj.associationAgentUsername = associationAgentUsername;
    obj.leagueName = leagueName;
    obj.seasonYear = seasonYear;
    obj.gameSettingPolicy = gameSettingPolicy;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function createReportRequestJson(refereeUsername, gameID, report) {
    let obj = new Object();
    obj.refereeUsername = refereeUsername;
    obj.gameID = gameID;
    obj.report = report;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

function addEventToGameRequestJson(refereeUsername, gameID, gameMinute, description, type, playerUsername) {
    let obj = new Object();
    obj.refereeUsername = refereeUsername;
    obj.gameID = gameID;
    obj.gameMinute = gameMinute;
    obj.description = description;
    obj.type = type;
    obj.playerUsername = playerUsername;
    let jsonString = JSON.stringify(obj);
    return jsonString;
}

