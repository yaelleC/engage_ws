package uws.engage.controller;

/**
 * This class contains the main keyWords of the DB
 * @author Yaelle Chaudy - University of the West of Scotland - yaelle.chaudy@uws.ac.uk
 * @version 2.0
 */
public class General {
	
	// ********************************** Parameters ********************************** //
	
		public boolean DEBUG = false;
		public boolean DEBUG_SQL = false;
	
		// --------------------------- Database codes --------------------------- //
		public String DB_NAME = (System.getenv("DATABASE_URL") != null)? "jdbc:" + System.getenv("DATABASE_URL") : "jdbc:mysql://mysql:3306/engage";
		public String DB_USERNAME = (System.getenv("DATABASE_USERNAME") != null)? System.getenv("DATABASE_USERNAME") : "root";
		public String DB_PASSWD = (System.getenv("DATABASE_USER_PASSWORD") != null)? System.getenv("DATABASE_USER_PASSWORD") : "123";
	
		// --------------------------- Table feedback --------------------------- //
		public String TABLE_FEEDBACK = "feedback";
		public String F_FIELD_ID = "id";
		public String F_FIELD_NAME = "name";
		public String F_FIELD_MESSAGE = "message";
		public String F_FIELD_TYPE = "type";
		public String F_FIELD_FINAL = "final";
		public String F_FIELD_WIN = "win";
		public String F_FIELD_ID_SG = "idSG";
		public String F_FIELD_VERSION_SG = "version";
				
		public String TABLE_FEEDBACK_TRIGGER = "feedback_trigger";
		public String FT_FIELD_ID_SG = "idSG";
		public String FT_FIELD_SG_VERSION = "version";
		public String FT_FIELD_ID_FDBK = "idFeedback";
		public String FT_FIELD_ID_OUTCOME = "idOutcome";
		public String FT_FIELD_INACTIVITY = "inactivity";
		public String FT_FIELD_LIMIT = "limitValue";
		public String FT_FIELD_INFERIOR = "inferior";
		public String FT_FIELD_REPEAT = "repeatBool";
		public String FT_FIELD_IMMEDIATE = "immediate";
				
		public String TABLE_BADGES_TRIGGER = "badges_trigger";
		public String BT_FIELD_ID_SG = "idSG";
		public String BT_FIELD_SG_VERSION = "version";
		public String BT_FIELD_ID_FDBK = "idFeedback";
		public String BT_FIELD_FUNCTION = "function";
		public String BT_FIELD_LIMIT = "limitValue";
		public String BT_FIELD_INFERIOR = "inferior";
		public String BT_FIELD_ID_OUTCOME = "idOutcome";
		
		// --------------------------- Table outcome --------------------------- //
		public String TABLE_OUTCOME = "learningoutcome";
		public String O_FIELD_ID = "id";
		public String O_FIELD_NAME = "name";
		public String O_FIELD_DESC = "description";
		public String O_FIELD_TYPE = "type";
		public String O_FIELD_VALUE = "startingValue";
		public String O_FIELD_ID_SG = "idSG";
		public String O_FIELD_VERSION_SG = "version";

		public String O_ENUM_KNOWLEDGE = "KNOWLEDGE";
		public String O_ENUM_SKILL = "SKILL";
		public String O_ENUM_COMPETENCE = "COMPETENCE";

		// --------------------------- Table seriousGame --------------------------- //
		public String TABLE_SG = "seriousgame";
		public String SG_FIELD_ID = "id";
		public String SG_FIELD_VERSION = "version";
		public String SG_FIELD_ID_DEVELOPER = "idDeveloper";
		public String SG_FIELD_ID_TEACHER = "idTeacher";
		public String SG_FIELD_NAME = "name";
		public String SG_FIELD_COMMENTS = "comments";
		public String SG_FIELD_NAME_VERSION = "nameVersion";
		public String SG_FIELD_DESC = "description";
		public String SG_FIELD_AGEMIN = "ageMin";
		public String SG_FIELD_AGEMAX = "ageMax";
		public String SG_FIELD_LANG = "language";
		public String SG_FIELD_COUNTRY = "country";
		public String SG_FIELD_PUBLIC = "public";
		public String SG_FIELD_CREATED = "created";
		public String SG_FIELD_UPDATED = "updated";
		public String SG_FIELD_CONFIG_SHORT = "configFile_short";
		public String SG_FIELD_CONFIG_LONG = "configFile_long";
		
		// --------------------------- Table gamePlay --------------------------- //		
		
		public String TABLE_GAMEPLAY = "gameplay";
		public String GP_FIELD_ID = "id";
		public String GP_FIELD_ID_SG = "idSG";
		public String GP_FIELD_VERSION = "version";
		public String GP_FIELD_CREATED = "created";
		public String GP_FIELD_ENDED = "ended";
		public String GP_FIELD_WIN = "win";
		public String GP_FIELD_ID_PLAYER = "idPlayer";
		public String GP_FIELD_LASTACTION = "lastAction";
		
		public String TABLE_GAMEPLAY_OUTCOME = "game_outcome";
		public String G_O_FIELD_ID_GP = "idGP";
		public String G_O_FIELD_ID_O = "idOutcome";
		public String G_O_FIELD_VALUE = "value";
				
		// --------------------------- Table actionSG --------------------------- //
		
		public String TABLE_ACTION_SG = "action_sg";
		public String A_FIELD_ID_SG = "idSG";
		public String A_FIELD_ACTION = "action";
		public String A_FIELD_PTS_DEFAULT = "defaultPts";
		public String A_FIELD_OUTCOME_DEFAULT = "defaultOutcome";
		public String A_FIELD_FEEDBACK_DEFAULT = "defaultFeedback";
		
		public String A_FIELD_OUTCOME_ID = "idOutcome";
		public String A_FIELD_PTS = "points";
		public String A_FIELD_ID = "id";
		public String A_FIELD_STRING_ID = "stringID";
		public String A_FIELD_FEEDBACK_TRIGGERED = "feedbackTriggered";
		public String A_FIELD_FEEDBACK_WHEN_ALL = "feedbackWhenAll";
		

		// --------------------------- Tables player --------------------------- //
		
		public String TABLE_PLAYER = "player_";
		public String P_FIELD_ID = "id";
		public String P_FIELD_SG_ID = "idSG";
		public String P_FIELD_ID_STUDENT = "idStudent";
		
		// --------------------------- Tables log --------------------------- //
		
		public String TABLE_LOG_GP_FEEDBACK = "log_gameplay_feedback";
		public String LOG_F_FIELD_ID_GP = "idGP";
		public String LOG_F_FIELD_ID_FEEDBACK = "idFeedback";
		public String LOG_F_FIELD_FEEDBACK = "feedback";
		public String LOG_F_FIELD_TIME = "time";
		
		public String TABLE_LOG_GP_ACTION = "log_gameplay_action";
		public String LOG_A_FIELD_ID_GP = "idGP";
		public String LOG_A_FIELD_ID_OUTCOME = "idOutcome";
		public String LOG_A_FIELD_ACTION = "action";
		public String LOG_A_FIELD_MARK = "mark";
		public String LOG_A_FIELD_TIME = "time";
		

		// --------------------------- Table developer --------------------------- //
		
		public String TABLE_DEVELOPER = "developer";
		public String DEV_FIELD_ID = "id";
		public String DEV_FIELD_NAME = "name";
		public String DEV_FIELD_SURNAME = "surname";
		public String DEV_FIELD_EMAIL = "email";
		public String DEV_FIELD_PASSWORD = "password";

		public String TABLE_SG_DEVELOPER = "sg_developer";
		public String SGDEV_FIELD_ID_SG = "idSG";
		public String SGDEV_FIELD_ID_DEV = "idDeveloper";
		
		
		// --------------------------- Table teacher --------------------------- //
		
		public String TABLE_TEACHER = "teacher";
		public String T_FIELD_ID = "id";
		public String T_FIELD_NAME = "name";
		public String T_FIELD_SURNAME = "surname";
		public String T_FIELD_EMAIL = "email";
		public String T_FIELD_PASSWORD = "password";
		public String T_FIELD_ID_SCHOOL = "idSchool";

		// --------------------------- Table student --------------------------- //
		
		public String TABLE_STUDENT = "student";
		public String STDT_FIELD_ID = "id";
		public String STDT_FIELD_USERNAME = "username";
		public String STDT_FIELD_PASSWORD = "password";
		public String STDT_FIELD_AGE = "dateBirth";
		public String STDT_FIELD_GENDER = "gender";
		public String STDT_FIELD_ID_SCHOOL = "idSchool";
		public String STDT_FIELD_ID_GROUP = "idGroup";

		// --------------------------- Table group --------------------------- //
		
		public String TABLE_GROUP = "group";
		public String GROUP_FIELD_ID = "id";
		public String GROUP_FIELD_ID_TEACHER = "idTeacher";

		// --------------------------- Table student_teacher --------------------------- //
		
		public String TABLE_STDT_TEACHER = "std_teacher";
		public String ST_FIELD_ID_STDT = "idStd";
		public String ST_FIELD_ID_TEACHER = "idTeacher";
		public String ST_FIELD_CLASS = "idGroup";

		// --------------------------- Table sg_student --------------------------- //
		
		public String TABLE_SG_STUDENT = "sg_student";
		public String SS_FIELD_ID_STUDENT = "idStd";
		public String SS_FIELD_ID_SG = "idSG";
		public String SS_FIELD_VERSION = "versionPlayed";

		// --------------------------- Table school --------------------------- //
		
		public String TABLE_SCHOOL = "school";
		public String S_FIELD_ID = "id";
		public String S_FIELD_NAME = "name";
		public String S_FIELD_COUNTRY = "country";
		public String S_FIELD_CITY = "city";

		// --------------------------- Table sg_school --------------------------- //
		
		public String TABLE_SG_SCHOOL = "sg_school";
		public String SGS_FIELD_ID_SG = "idSG";
		public String SGS_FIELD_ID_SCHOOL = "idSchool";
		
		// --------------------------- Int returns  --------------------------- //
		public int CST_RETURN_SUCCESS = 1;
		public int CST_RETURN_WRONG_ID = -1;
		public int CST_RETURN_SQL_ERROR = -2;
		public int CST_RETURN_ALREADY_ENDED = -0;
		
		public int CST_RETURN_ERROR_PROTOCOL = 400;


		// --------------------------- Communication Protocol  --------------------------- //
		public String CST_SEPARATOR = ",,,";
		public String CST_ATTRIBUTION = "===";
		public String CST_END = "///";
}
