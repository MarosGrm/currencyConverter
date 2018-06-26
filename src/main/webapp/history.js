import {fillInTable} from "./commonUtils.js";

$(document).ready(function() {
    fillInTable('/GetStartedJava/api/history/all', $('#allHistoryTable'));
});