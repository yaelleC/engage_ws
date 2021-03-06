define({ "api": [
  {
    "type": "post",
    "url": "/SGaccess",
    "title": "Login Student or Guest",
    "description": "<p>A teacher can modify your game’s assessment, thus creating a new version of the game.  Before the player can start the game, you need to check their credential (username and password)  and get the version the teacher decided they would play. You do so by invoking the <code>SGaccess</code> web service.  It will also return a list of caracteristics you need to know about the player  (based on the Player section of the configuration file).</p> ",
    "name": "1PostLogin",
    "group": "1OutsideGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "json",
            "optional": false,
            "field": "loginData",
            "description": "<p>JSON with three key/value: <ul><li><i>idSG </i>: int, ID of current game  </li><li><i>username </i>: string, username of player, empty for guest login </li><li><i>password </i>: string, password of player, empty for guest login </li></ul></p> "
          }
        ]
      },
      "examples": [
        {
          "title": "player login",
          "content": "{\n  \"idSG\": 92,\n  \"username\": \"yaelle\",\n  \"password\": \"password\"\n}",
          "type": "json"
        },
        {
          "title": "guest login",
          "content": "{\n  \"idSG\": 92,\n  \"username\": \"\",\n  \"password\": \"\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "loginResult",
            "description": "<p>JSON with: <ul><li><i>loginSuccess </i>: true if username/password exists       </li><li><i>params </i>: list of information to ask about the player, if any       </li><li><i>version </i>: version of the game to be played by the player       </li><li><i>idPlayer </i>: ID of the player if he/she has played before       </li><li><i>student </i>: basic info about the student logged in </li></ul></p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Wrong/Guest login, public game",
          "content": "{\n     \"loginSuccess\": false,\n     \"params\": [\n         {\n             \"name\": \"age\",\n             \"question\": \"How old are you?\",\n             \"type\": \"Int\"\n         },\n         {\n             \"name\": \"gender\",\n             \"type\": \"Char\"\n         }\n     ],\n     \"version\": 0\n }",
          "type": "json"
        },
        {
          "title": "Wrong login, non-public game",
          "content": "{  \n   \"loginSuccess\": false\n}",
          "type": "json"
        },
        {
          "title": "Correct login, known player",
          "content": "{  \n        \"idPlayer\": 2,\n        \"student\": {\n            \"id\": 9,\n            \"username\": \"yaelle\",\n            \"idSchool\": 1,\n            \"dateBirth\": \"1988-08-17\"\n        },\n        \"loginSuccess\": true,\n        \"params\": [],\n        \"version\": 0\n}",
          "type": "json"
        },
        {
          "title": "Correct login, new student",
          "content": "{  \n        \"student\": {\n            \"id\": 1,\n            \"username\": \"test\",\n            \"idSchool\": 1,\n            \"dateBirth\": \"2000-01-01\"\n        },\n        \"loginSuccess\": true,\n        \"params\":[\n           {\n                \"name\": \"age\",\n                \"type\": \"Int\"\n           },\n           {\n               \"name\": \"gender\",\n               \"type\": \"Char\"\n           }\n       ],\n       \"version\": 0\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/SeriousGameAccessResource.java",
    "groupTitle": "Outside the gameplay"
  },
  {
    "type": "get",
    "url": "/seriousgame/info/:idSG/version/:version",
    "title": "Get Game Data",
    "description": "<p>Because a teacher might modify some of the game information (description, age range...),  you may need to access the game data at some point in your game (e.g. an &quot;about&quot; window). <br/> To retrieve this, you need to call this web service</p> ",
    "name": "2GetGameInfo",
    "group": "1OutsideGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the current game</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "gameData",
            "description": "<p>A json describing the current game (name, description... as defined in the configuration file)</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example of response",
          "content": "\"seriousGame\": {\n     \"genre\": \"Runner\",\n     \"idDeveloper\": 1,\n     \"ageMin\": 10, \n     \"ageMax\": 99,\n     \"description\": \"This is a mini game that trains you to identify the countries that form the European Union\",\n     \"subject\": \"geography\",\n     \"name\": \"EU mouse\",\n     \"public\": true,\n     \"lang\": \"EN\",\n     \"country\": \"UK\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/SeriousGameResource.java",
    "groupTitle": "Outside the gameplay"
  },
  {
    "type": "get",
    "url": "/seriousgame/:idSG/version/:version/actionParam/{action}",
    "title": "Get Parameters of action",
    "description": "<p>Because a teacher might modify the possible values for an action&#39;s parameters,  you may need to access the list of possible values at some point in your game  (e.g. when you generate enemies, or words to translate). <br/> To retrieve this, you need to call this web service</p> ",
    "name": "3GetParametersOfAction",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "action",
            "description": "<p>to retrieve the values from</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "actionParams",
            "description": "<p>A json listing the possible values for the action&#39;s parameters</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example of response for action 'translate'",
          "content": "[\n     { \"wordInFrench\": \"poire\", \"wordInEnglish\": \"pear\" },\n     { \"wordInFrench\": \"pomme\", \"wordInEnglish\": \"apple\" },\n     { \"wordInFrench\": \"fraise\", \"wordInEnglish\": \"strawberry\" },\n]",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/SeriousGameResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "get",
    "url": "/gameplay/:idGP/feedback",
    "title": "Get Feedback",
    "description": "<p>If a feedback is triggered by an action you would receive it after invoking the assess web service.  For any other feedback triggered (inactivity or score related) you can invoke this web service.</p> ",
    "name": "GetFeedback",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idGP",
            "description": "<p>ID of the current gameplay</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "Feedback",
            "description": "<p>list of feedback to be triggered in the current gameplay</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Response type",
          "content": "[\n   {\n       \"message\": String,\n       \"id\": integer,\n       \"name\": String,\n       \"final\": String (\"true\", \"win\", \"lose\")\n       \"type\": String (POSITIVE, NEGATIVE, NEUTRAL, BADGE or HINT)\n   },\n   …\n ]",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "get",
    "url": "/gameplay/:idGP/score/:idScore",
    "title": "Get Score",
    "description": "<p>To retrieve the current values of the learning outcome scores, invoke this web service.</p> ",
    "name": "GetScore",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idGP",
            "description": "<p>ID of the current gameplay</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idScore",
            "description": "<p>ID of the specific score to lookup</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "Score",
            "description": "<p>the score of ID idScore for the current gameplay (float)</p> "
          }
        ]
      }
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "get",
    "url": "/gameplay/:idGP/scores",
    "title": "Get Scores",
    "description": "<p>To retrieve the current values of the learning outcome scores, invoke this web service.</p> ",
    "name": "GetScores",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idGP",
            "description": "<p>ID of the current gameplay</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "Scores",
            "description": "<p>the scores of the current gameplay</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "[\n     {\n         \"id\": 183,\n         \"startingValue\": 28,\n         \"description\": \"distinct countries of the EU left to find\",\n         \"name\": \"eu_countries\",\n         \"value\": 15\n     },\n     {\n         \"id\": 184,\n         \"startingValue\": 3,\n         \"description\": \"number of lives the player has\",\n         \"name\": \"lives\",\n         \"value\": 2\n     }\n ]",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "post",
    "url": "/gameplay/:idGameplay/AssessAndScore",
    "title": "Assess",
    "description": "<p>During the gameplay, you will want to assess every player&#39;s action,  you can do so by invoking the <code>assess</code> web service as follows.</p> ",
    "name": "PostAssess",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idGameplay",
            "description": "<p>ID of the current gameplay</p> "
          },
          {
            "group": "Parameter",
            "type": "json",
            "optional": false,
            "field": "actionData",
            "description": "<p>JSON object containing the action to assess (name and parameters)</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example one parameter",
          "content": "{  \n   \"action\": \"collectEUcountries\",\n   \"values\": {\n       \"country\": \"France\"\n   }\n}",
          "type": "json"
        },
        {
          "title": "Example several parameters",
          "content": "{  \n   \"action\": \"translateToItalian\",\n   \"values\": {\n       \"englishWord\": \"Hello\",\n       \"italianWord\": \"Buongiorno\",\n   }\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "feedbackAndScore",
            "description": "<p>A JSON object containing feedback to trigger (if any) and the new score values</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "{\n     \"feedback\": [\n         {   \"message\": \"Yes, France is indeed part of the EU\",\n             \"name\": \"correct_country\",\n             \"type\": \"POSITIVE\"\n         }\n     ],\n     \"scores\": [\n         {   \"startingValue\": 28,\n             \"description\": \"countries of the EU left to find\",\n             \"name\": \"eu_countries\",\n             \"value\": 27\n         },\n         {   \"startingValue\": 3,\n             \"description\": \"number of lives the player has\",\n             \"name\": \"lives\",\n             \"value\": 3\n         }\n     ]\n }",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "post",
    "url": "/gameplay/:idGP/end/:end",
    "title": "End gameplay",
    "description": "<p>When the player stops or finishes the gameplay,  notify the engine by invoking the <code>end</code> web service. This will update the database allowing for accurate calculation of the time spent playing. Some badges are based on the number of gameplays won so it is crucial to save the data.</p> ",
    "name": "PostEndGameplay",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idGP",
            "description": "<p>ID of the current gameplay</p> "
          },
          {
            "group": "Parameter",
            "type": "String",
            "allowedValues": [
              "\"win\"",
              "\"lose\"",
              "\"end\""
            ],
            "optional": false,
            "field": "end",
            "description": "<p>specifies whether the game was won, lost or simply ended</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number",
            "allowedValues": [
              "0",
              "1",
              "-1"
            ],
            "optional": false,
            "field": "endStatus",
            "description": "<p>returns </p> <ul><li>1 if the gameplay has ended correctly</li><li>0 if the game had already ended</li><li>-1 is the game doesn&#39;t exist</li></ul>"
          }
        ]
      }
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "post",
    "url": "/gameplay/startGP",
    "title": "Start a Gameplay",
    "description": "<p>When a player starts playing your game, you need to tell the engine to set up a new gameplay.  That way, new scores will be set for your learning outcomes and the player&#39;s actions will be associated to them.  In order to create a new gameplay, you need to invoke the <code>gameplay</code> web service as follows.</p> ",
    "name": "StartGameplay",
    "group": "2Gameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "json",
            "optional": false,
            "field": "gamePlayerData",
            "description": "<p>JSON object containing game and player data <br/> <i>If idStudent is 0, the game is started for a guest. </i></p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Known Player",
          "content": "{  \n   \"idSG\": 92,\n   \"version\": 0,\n   \"idPlayer\": 2\n}",
          "type": "json"
        },
        {
          "title": "New Player",
          "content": "{ \n    \"idSG\": 92,\n    \"version\": 0,\n    \"idStudent\": 9,\n    \"params\": [\n       {   \n           \"name\": \"age\",\n           \"type\": \"Int\",\n           \"value\": 15\n       },\n       {   \n            \"name\": \"gender\",\n            \"type\": \"Char\",\n            \"value\": \"m\"\n       }\n    ]\n}",
          "type": "json"
        },
        {
          "title": "Guest",
          "content": "{ \n    \"idSG\": 92,\n    \"version\": 0,\n    \"idStudent\": 0,\n    \"params\": [\n       {   \n           \"name\": \"age\",\n           \"type\": \"Int\",\n           \"value\": 26\n       },\n       {   \n            \"name\": \"gender\",\n            \"type\": \"Char\",\n            \"value\": \"f\"\n       }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "gameplayData",
            "description": "<p>json with ID of gameplay created and ID of player</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "{\n    \"idGameplay\": 183,\n    \"idPlayer\": 28\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "Gameplay"
  },
  {
    "type": "get",
    "url": "/badges/all/seriousgame/:idSG/version/:version/player/:idPlayer",
    "title": "Get All Badges",
    "description": "<p>At some point in your game, you might want to display the badges earned by the player logged in. <br/>  This function returns all the badges available in the game and specify if the player earned it.</p> ",
    "name": "GetAllBadges",
    "group": "3AfterGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idPlayer",
            "description": "<p>ID of the current player</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "listBadges",
            "description": "<p>A list of badges the player has earned, each badge has: </p> <ul><li>A<i> message </i> : string </li><li> An<i> id </i> : integer</li><li> A<i> name </i> : string </li></ul>"
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "[\n    {\n        \"message\": \"You played 10+ times\",\n        \"id\": 466,\n        \"name\": \"effort\",\n        \"earned\": false,\n        \"goalNum\": 10,\n        \"playerNum\": 8\n    },\n    {\n        \"message\": \"You found 50 EU countries\",\n        \"id\": 469,\n        \"name\": \"bronze_medal\",\n        \"earned\": true,\n        \"goalNum\": 50,\n        \"playerNum\": 76\n    }\n]",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/BadgesResource.java",
    "groupTitle": "After the gameplay(s)"
  },
  {
    "type": "get",
    "url": "/badges/seriousgame/:idSG/version/:version/player/:idPlayer",
    "title": "Get Badges",
    "description": "<p>At some point in your game, you might want to display the badges earned by the player logged in. <br/> To retrieve them, you need to call this web service</p> ",
    "name": "GetBadges",
    "group": "3AfterGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the current game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idPlayer",
            "description": "<p>ID of the current player</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "listBadges",
            "description": "<p>A list of badges the player has earned, each badge has: </p> <ul><li>A<i> message </i> : string </li><li> An<i> id </i> : integer</li><li> A<i> name </i> : string </li></ul>"
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "[\n    {\n        \"message\": \"You played 10+ times\",\n        \"id\": 466,\n        \"name\": \"effort\"\n    },\n    {\n        \"message\": \"You found 50 EU countries\",\n        \"id\": 469,\n        \"name\": \"bronze_medal\"\n    }\n]",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/BadgesResource.java",
    "groupTitle": "After the gameplay(s)"
  },
  {
    "type": "get",
    "url": "/learninganalytics/leaderboard/seriousgame/:idSG/version/:version",
    "title": "Get LeaderBoard",
    "description": "<p>EngAGe also gives you access to the game&#39;s leader board.  To retrieve it, you need to call this web service.</p> ",
    "name": "GetLeaderBoard",
    "group": "3AfterGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the game to retrieve the leaderboard from</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the game</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "gameLearderboard",
            "description": "<p>game leaderboard: JSON object containing an array of performances (15 max) for each score of the game.  The performances are in descending order and are composed of a name and a score.  The leaderboard also include the longest and shortest times (in seconds).</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "{\n     \"score1\": [\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 74\n         },\n         {\n             \"name\": \"yaelle\",\n             \"score\": 38\n         }\n     ],\n     \"score2\": [\n         {\n             \"name\": \"yaelle\",\n             \"score\": 129\n         },\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 112\n         }\n     ], \n    \"longestGameplays\": [\n         {\n             \"name\": \"Yaelle\",\n             \"score\": 77\n         },\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 40\n         }\n     ],\n     \"shortestGameplays\": [\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 6\n         },\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 40\n         }\n     ]\n }",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/LearningAnalyticsResource.java",
    "groupTitle": "After the gameplay(s)"
  },
  {
    "type": "get",
    "url": "/learninganalytics/leaderboard/:limit/seriousgame/:idSG/version/:version",
    "title": "Get LeaderBoard (limit)",
    "description": "<p>EngAGe allows you to specify the number of results you want for the game&#39;s leader board.</p> ",
    "name": "GetLeaderBoardLimit",
    "group": "3AfterGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the game to retrieve the leaderboard from</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the game</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "limit",
            "description": "<p>maximum number of performances to retrieve</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "gameLearderboard",
            "description": "<p>game leaderboard: JSON object containing an array of performances (max :limit) for each score of the game.  The performances are in descending order and are composed of a name and a score.  The leaderboard also include the longest and shortest times (in seconds).</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example",
          "content": "{\n     \"score1\": [\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 74\n         },\n         {\n             \"name\": \"yaelle\",\n             \"score\": 38\n         }\n     ],\n     \"score2\": [\n         {\n             \"name\": \"yaelle\",\n             \"score\": 129\n         },\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 112\n         }\n     ], \n    \"longestGameplays\": [\n         {\n             \"name\": \"Yaelle\",\n             \"score\": 77\n         },\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 40\n         }\n     ],\n     \"shortestGameplays\": [\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 6\n         },\n         {\n             \"name\": \"Anonymous\",\n             \"score\": 40\n         }\n     ]\n }",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/LearningAnalyticsResource.java",
    "groupTitle": "After the gameplay(s)"
  },
  {
    "type": "get",
    "url": "/gameplay/:idGP/log",
    "title": "Get Log",
    "description": "<p>After the gameplay, you might want to present the player with a final summative feedback,  to do so you can use the logs that EngAGe keeps of gameplays.</p> ",
    "name": "GetLog",
    "group": "3AfterGameplay",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idGP",
            "description": "<p>ID of the current gameplay</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "gameplayLog",
            "description": "<p>JSON object listing actions and feedback of the gameplay</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Response type",
          "content": "{\n   \"actionLog\": [\n     {\n       \"time\": timestamp (yyyy-MM-dd hh:mm:ss.s),\n       \"action\": {\n           \"values\": {\n               “name”: “value”, …\n           },\n           \"action\": String\n       },\n       \"mark\": integer,\n       \"idOutcome\": integer\n     }, …\n   ],\n   \"feedbackLog\": [\n     {\n       \"feedback\": {\n           \"id\": integer,\n           \"message\": String,\n           \"name\": String,\n           \"type\": String (POSITIVE, NEGATIVE, NEUTRAL, BADGE or HINT)\n       },\n       \"time\": timestamp (yyyy-MM-dd hh:mm:ss.s)\n     },…\n   ]\n }",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/GamePlayResource.java",
    "groupTitle": "After the gameplay(s)"
  },
  {
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "varname1",
            "description": "<p>No type.</p> "
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "varname2",
            "description": "<p>With type.</p> "
          }
        ]
      }
    },
    "type": "",
    "url": "",
    "version": "0.0.0",
    "filename": "./doc/main.js",
    "group": "C__Users_Yaelle_Documents_PhD_engage_ws_doc_main_js",
    "groupTitle": "C__Users_Yaelle_Documents_PhD_engage_ws_doc_main_js",
    "name": ""
  },
  {
    "type": "get",
    "url": "/learninganalytics/seriousgame/:idSG/version/:version",
    "title": "Get Learning Analytics",
    "description": "<p>This web service returns extensive information about the players and the gameplays.  Every action of every player is listed in the JSON returned, along with timestamp and how it affected the score.</p> ",
    "name": "GetLearningAnalytics",
    "group": "LearningAnalytics",
    "version": "2.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "idSG",
            "description": "<p>ID of the game to retrieve LA from</p> "
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "version",
            "description": "<p>version number of the current game</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "json",
            "optional": false,
            "field": "LAData",
            "description": "<p>all the learning analytics data available on the game</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Example type",
          "content": "{\n    \"players\": [\n        {\n            \"idPlayer\": 1,\n            \"age\": \"26\",\n            \"gender\": \"f\",\n            \"idStudent\": 1\n        }],\n    \"gameplays\": [\n        \"id\": 600,\n        \"idPlayer\": 1,\n        \"timeStarted\": \"2015-03-12 20:35:01.0\",\n         \"finalScores\": {\n            \"eu_countries\": 16, \"lives\": 0\n        },\n        \"timeSpent\": 93,\n        \"actions\": [\n         {\n             \"timestamp\": 4,\n             \"action\": \"newCountrySelected\",\n             \"mark\": -1,\n             \"parameters\": {\n                 \"country\": \"switzerland\"\n             },\n             \"outcome\": \"lives\",\n             \"valuesLog\": null\n         }, ...\n      ],\n      \"game\": {\n         \"genre\": \"runner\",\n         \"idDeveloper\": 200,\n         \"learningOutcomes\": {\n             \"eu_countries\": {\n                 \"desc\": \"the number of EU countries left to find\",\n                  \"feedbackTriggered\": [\n                      {\n                         \"limit\": 1,\n                         \"sign\": \"<\",\n                         \"feedback\": [\n                             {\n                                 \"immediate\": true,\n                                 \"name\": \"endWin\"\n                             }\n                         ]\n                     }\n                 ],\n                 \"value\": 28\n             },\n            \"lives\": {\n                 \"desc\": \"the number left to the player\",\n                 \"feedbackTriggered\": [\n                     {\n                         \"limit\": 1,\n                          \"sign\": \"<\",\n                         \"feedback\": [\n                             {\n                                 \"immediate\": true,\n                                 \"name\": \"endLose\"\n                              }\n                         ]\n                     }\n                 ],\n                 \"value\": 3\n             }\n         },\n         \"subject\": \"Europe, Capitals, Geography\",\n         \"ageMax\": 99,\n         \"playerCharacteristics\": [\n             \"age\",\n             \"gender\"            \n         ],\n         \"lang\": \"EN\",\n         \"country\": \"UK\",\n         \"version\": 0,\n         \"id\": 95,\n         \"ageMin\": 10,\n         \"description\": \"Find european capitals\",\n         \"name\": \"EuMouse\",\n         \"public\": \"false\"\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/uws/engage/assessment/LearningAnalyticsResource.java",
    "groupTitle": "Learning Analytics"
  }
] });