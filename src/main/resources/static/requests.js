const baseURL= "http://lvh.me:5001/api/v1";


//-------------------*direct use cases requests*:--------------
//-----------------------------login:--------------------------
async function loginRequest(userName, password, firstName, lastName) {
    let url = baseURL + "/login";
    let jsonString = loginRequestJson(userName, password, firstName, lastName);
    let response = await sendPostReq(url, jsonString);
    return response;
}

//-----------------------------logout:--------------------------
async function logoutRequest(userName) {
    let url = baseURL + "/logout";
    let jsonString = logoutRequestJson(userName);
    let response = await sendPostReq(url, jsonString);
    return response;
}

//-----------------------------Add team:--------------------------
async function addTeamRequest(ownerUserName, teamName, fieldCountry, fieldState, fieldCity, fieldPostalCode) {
    let url = baseURL+ "/addTeam";
    let jsonString = teamRequestJson(ownerUserName,teamName,fieldCountry,fieldState,fieldCity,fieldPostalCode);
    let response = await sendPostReq(url, jsonString); 
    return response; 
}

//-----------------------------Define Game Setting Policy::---------
async function setGameSettingPolicyRequest(associationAgentUsername, leagueName, seasonYear, gameSettingPolicy) {
    let url = baseURL + "/setGameSettingPolicy";
    let jsonString = gameSettingPolicyRequestJson(associationAgentUsername,leagueName,seasonYear,gameSettingPolicy);
    let response = await sendPostReq(url, jsonString); 
    return response; 
}

//-----------------------Define Game Ranking Policy:-------
async function setRankingPolicyRequest(associationAgentUsername, leagueName, seasonYear, win, goal, draw, yellowCards, redCards) {
    let url = baseURL + "/setRankingPolicy";
    let jsonString = rankingPolicyRequestJson(associationAgentUsername,leagueName,seasonYear,win,goal,draw,yellowCards,redCards);
    let response = await sendPostReq(url, jsonString); 
    return response; 
}

// ------------------------Referee Adds Events To Game------
async function addEventToGameRequest(refereeUsername, gameID, gameMinute, description, type, playerUsername) {
    let url = baseURL + "/addEventToGame";
    let jsonString = addEventToGameRequestJson(refereeUsername,gameID,gameMinute,description,type,playerUsername);
    let response = await sendPostReq(url, jsonString); 
    return response;
}

// ------------------------Referee Create Game Report-------
async function createGameReportRequest(refereeUsername, gameID, report) {
    let url = baseURL + "/createGameReport";
    let jsonString = createReportRequestJson(refereeUsername,gameID,report);
    let response = await sendPostReq(url, jsonString); 
    return response;
}

//---------------------*data geters*:-----------
// -------------------get all leagues:----------
async function getLeagueNamesRequest() {
    let url = baseURL + "/getLeagueNames";
    let response = await sendNoParamsGet(url);
    return response;
}

//------------------------getRefereeActiveGame:-----
async function getRefereeActiveGameRequest(refereeUsername) {
    let url = baseURL + "/getRefereeActiveGame";
    let params = "refereeUsername=" + refereeUsername;
    let response = await sendParamsGet(url,params);
    return response;

}

async function getRefereeReportActiveGame(refereeUsername){
    let url = baseURL + "/getRefereeReportActiveGame";
    let params = "refereeUsername=" + refereeUsername;
    let response = await sendParamsGet(url,params);
    return response;
}

//------------------------getAllTeamPlayers:-----
async function getAllTeamPlayersRequest(teamName) {
    let url = baseURL + "/getTeamPlayerNames";
    let params = "teamName=" + teamName;
    let response = await sendParamsGet(url,params);
    return response;

}

//---------------getAllLeagueSeasons:-------------
async function getAllLeagueSeasons(league) {
    let url = baseURL + "/getAllLeagueSeasons";
    let params = "leagueName=" + league;
    let response = await sendParamsGet(url,params);
    return response;
}

// ------------------*system operations*----------
// ------------------reset system:----------
async function resetSystemRequest(sysManagerUserName) {
    let url = baseURL + "/resetSystem";
    let jsonString = resetSystemReqJson(sysManagerUserName);
    let response = await sendPostReq(url,jsonString);
    return response;
}














