package uws.engage.assessment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import uws.chaudy.EngageStandaloneSetup;
import uws.chaudy.controller.ActionLogController;
import uws.chaudy.controller.DeveloperController;
import uws.chaudy.controller.FeedbackController;
import uws.chaudy.controller.FeedbackLogController;
import uws.chaudy.controller.FeedbackTriggerController;
import uws.chaudy.controller.GamePlayController;
import uws.chaudy.controller.LearningOutcomeController;
import uws.chaudy.controller.PlayerController;
import uws.chaudy.controller.SchoolController;
import uws.chaudy.controller.SeriousGameController;
import uws.chaudy.controller.StudentController;
import uws.chaudy.controller.TeacherController;
import uws.chaudy.engage.Action;
import uws.chaudy.engage.ActionReaction;
import uws.chaudy.engage.Characteristic;
import uws.chaudy.engage.EvidenceModel;
import uws.chaudy.engage.Feedback;
import uws.chaudy.engage.FeedbackMessages;
import uws.chaudy.engage.FeedbackModel;
import uws.chaudy.engage.GameDescription;
import uws.chaudy.engage.LearningOutcomes;
import uws.chaudy.engage.Model;
import uws.chaudy.engage.Outcome;
import uws.chaudy.engage.Parameter;
import uws.chaudy.engage.Params;
import uws.chaudy.engage.PlayerDescription;
import uws.chaudy.engage.Point;
import uws.chaudy.engage.Points;
import uws.chaudy.engage.Reaction;
import uws.chaudy.engage.TimerAction;
import uws.chaudy.engage.TimerActions;
import uws.chaudy.engage.Timing;
import uws.chaudy.engage.Trigger;
import uws.chaudy.engage.TriggerFeedback;

import org.json.simple.JSONObject;


import com.google.inject.Injector;



public class Engage {
	
	public static void main(String[] args) throws Exception {
		
		String dsl = readFile("configFile2.txt");		
		Engage engage = new Engage();
		JSONObject json = engage.getJSONfromDSL(dsl);

		System.out.println(json.toJSONString());
	}
	
	// ********************** Users/Entities CRUD ********************** //
		
	// ##### Developer ##### //
	
	public int createDeveloper(JSONObject developer) throws Exception
	{
		DeveloperController devController = new DeveloperController();
		return devController.createDeveloper(developer);
	}
	
	public int updateDeveloper(JSONObject developer) throws Exception
	{
		DeveloperController devController = new DeveloperController();
		return devController.updateDeveloper(developer);		
	}
	
	public int deleteDeveloper (int idDevelooper) throws Exception
	{
		DeveloperController devController = new DeveloperController();
		return devController.deleteDeveloper(idDevelooper);
	}
	
	// ##### Teacher ##### //
	
	public int createTeacher(JSONObject teacher) throws Exception
	{
		TeacherController teacherController = new TeacherController();
		return teacherController.createTeacher(teacher);
	}
	
	public int updateTeacher(JSONObject teacher) throws Exception
	{
		TeacherController teacherController = new TeacherController();
		return teacherController.updateTeacher(teacher);
	}
	
	public int deleteTeacher(int idTeacher) throws Exception
	{
		TeacherController teacherController = new TeacherController();
		return teacherController.deleteTeacher(idTeacher);
	}
	
	
	// ##### School ##### //
	
	public int createSchool(JSONObject school) throws Exception
	{
		SchoolController schoolController = new SchoolController();
		return schoolController.createSchool(school);
	}
	public int updateSchool(JSONObject school) throws Exception
	{
		SchoolController schoolController = new SchoolController();
		return schoolController.updateSchool(school);
	}
	public int deleteSchool(int idSchool) throws Exception
	{
		SchoolController schoolController = new SchoolController();
		return schoolController.deleteSchool(idSchool);
	}
	public JSONObject getSchoolById(int idSchool) throws Exception
	{
		SchoolController schoolController = new SchoolController();
		return schoolController.getSchoolById(idSchool);
	}
	
	// ##### Student ##### //
	
	public int createStudent(JSONObject student) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.createStudent(student);
	}
	public int updateStudent(JSONObject student) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.updateStudent(student);
	}
	public int deleteStudent(int idStudent) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.deleteStudent(idStudent);
	}
	public JSONObject getStudentById(int idStudent) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.getStudentsByID(idStudent);
	}
	
	public int addStudentToTeacher(int idStudent, int idTeacher) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.addStudentToTeacher(idStudent, idTeacher);		
	}
	
	public int addStudentToTeacher(int idStudent, int idTeacher, String className) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.addStudentToTeacher(idStudent, idTeacher, className);		
	}
	public int removeStudentFromTeacher(int idStudent, int idTeacher)throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.removeStudentFromTeacher(idStudent, idTeacher);	
	}
	public int removeStudentFromTeacherByClass(int idStudent, int idTeacher, String className)throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.removeStudentFromTeacherByClass(idStudent, idTeacher, className);	
	}
	public ArrayList<JSONObject> getStudentsOfTeacher(int idTeacher) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.getStudentsOfTeacher(idTeacher);		
	}
	public ArrayList<JSONObject> getStudentsOfTeacherByClass(int idTeacher, String group) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.getStudentsOfTeacherByClass(idTeacher, group);
	}
	
	
	// ********************** Authentification ********************** //
	
	
	/**
	 * Check a developer's login details
	 * @param email
	 * @param password
	 * @return the developer's details if the login is correct, null otherwise
	 * @throws Exception 
	 */
	public JSONObject loginDeveloper(String email, String password) throws Exception
	{
		DeveloperController devController = new DeveloperController();
		return devController.checkDeveloperEmailAndPassword(email, password);
	}
	
	
	/**
	 * Check a teacher's login details
	 * @param email
	 * @param password
	 * @return the teacher's details if the login is correct, null otherwise
	 * @throws Exception
	 */
	public JSONObject loginTeacher(String email, String password) throws Exception
	{
		TeacherController teacherController = new TeacherController();
		return teacherController.checkTeacherEmailAndPassword(email, password);
	}

	
	/**
	 * Check a student's login details
	 * @param username
	 * @param password
	 * @return the game's version to be played by student if the login is successful, -1 otherwise
	 * @throws Exception
	 */
	public int loginStudent(String username, String password, int idSG) throws Exception
	{
		StudentController stdtController = new StudentController();
		JSONObject student = stdtController.checkStudentUsernameAndPassword(username, password);
		if (student != null)
		{
			return getStudentVersionOfSG(idSG, Integer.parseInt(student.get("id").toString()));
		}
		else
		{
			return -1;
		}
	}
	
	
	// ********************** Give/Check access to SGs ********************** //
	
	public int giveSGaccessToSchool(int idSG, int idSchool) throws Exception
	{
		SchoolController schoolController = new SchoolController();
		return schoolController.giveSGaccessToSchool(idSG, idSchool);
	}
	
	public int giveSGaccessToStudent(int idSG, int idStudent, int version) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.giveSGaccessToStudent(idSG, idStudent, version);
	}
	
	/**
	 * If the student has access to the game it returns the version to be played, else -1 is returned
	 * @param idSG
	 * @param idStudent
	 * @return int version to be played or -1
	 */
	public int getStudentVersionOfSG(int idSG, int idStudent) throws Exception
	{
		StudentController stdtController = new StudentController();
		return stdtController.getStudentVersionOfSG(idSG, idStudent);
	}
	
	// ********************** Serious Game Creation and Versioning ********************** //
		
	private JSONObject getJSONfromDSL(String dsl) throws IOException
	{
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
		Injector injector = new EngageStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		Resource resource = resourceSet.createResource(URI.createURI("file:/example.assess"));
		InputStream in = new ByteArrayInputStream(dsl.getBytes());
		resource.load(in, resourceSet.getLoadOptions());
		Model model = (Model) resource.getContents().get(0);	
		
		return DSLtoJSON(model);
	}
	
	public int createSG (String dsl) throws Exception
	{
		JSONObject configFile = getJSONfromDSL(dsl);
		return createSG(configFile);
	}
	
	private int createSG (JSONObject configFile) throws Exception
	{
		
		
		// ##################### SERIOUS GAME TABLE ######################		
		// Create a row in the seriousgame table
				
		SeriousGameController SGcontroller = new SeriousGameController();
		JSONObject sg = (JSONObject)configFile.get("seriousGame");
		int idSG = SGcontroller.createSeriousGame(sg);
		int version = 0;
		if (sg.containsKey("id"))
		{
			idSG = Integer.parseInt(sg.get("id").toString());
			version = Integer.parseInt(sg.get("version").toString());
		}
		
		System.out.println(idSG);
		System.out.println(version);
		
		if (idSG > 0)
		{			
			// ##################### PLAYER TABLE ######################		
			// Create a table player based on the SG player's characteristics
			
			PlayerController playerController = new PlayerController();
			playerController.createTablePlayer((ArrayList<JSONObject>)configFile.get("player"), idSG, version);
					
			
			// ##################### FEEDBACK MESSAGES TABLE ######################
			// Creates rows in table feedback message linked to this SG
						
			JSONObject feedback = (JSONObject) configFile.get("feedback");
			FeedbackController fdbckController = new FeedbackController();
			
			Set<String> keys = feedback.keySet();
			for (String key : keys)
			{
				fdbckController.createFeedback(key, (JSONObject)feedback.get(key), idSG, version);
			}
			
			
			// ##################### FEEDBACK_TRIGGER TABLE ######################
			// Creates rows in table feedback_trigger describing the feedback model of this SG

			FeedbackTriggerController triggerController = new FeedbackTriggerController();
			ArrayList<JSONObject> triggersInactivity = (ArrayList<JSONObject>) configFile.get("inactivityFeedback");
			
			for (JSONObject t : triggersInactivity) {
				triggerController.addFeedbackTriggerInactivity(idSG, t, version);
			}
			
			
			
			// ##################### LEARNING_OUTCOME / FEEDBACK_TRIGGER TABLES ######################
			// Creates rows in table learning outcome linked to the SG
			
			JSONObject los = (JSONObject) configFile.get("learningOutcomes");
			LearningOutcomeController LOController = new LearningOutcomeController();
			
			keys = los.keySet();
			for (String key : keys) 
			{
				JSONObject lo = (JSONObject) los.get(key);
				int idLO = LOController.createLearningOutcome(key, lo, idSG, version);
				
				if (lo.containsKey("feedbackTriggered"))
				{
					// create rows in table feedback_trigger 
					ArrayList<JSONObject> triggers = (ArrayList<JSONObject>) lo.get("feedbackTriggered");
					for (JSONObject t : triggers) {
						triggerController.addFeedbackTriggerOutcome(idSG, idLO, t, version);
					}
				}
			}
			
			// ##################### SAVE CONFIG FILE ######################
			// Update SG table to save config file
			
			SGcontroller.saveCF(configFile.toJSONString(), idSG);
				
		}
		
		return idSG;
	}
	
	public int updateSG (JSONObject configFile) throws Exception
	{
		return createSG(configFile);
	}
	
	public JSONObject getConfigFile ( int idSG, int version ) throws Exception
	{
		SeriousGameController SGcontroller = new SeriousGameController();
		return SGcontroller.getConfigFile(idSG, version);
	}
	

	
	
	// ********************** Game play ********************** //
	
	public ArrayList<JSONObject> getParametersRequired(int idStudent, int idSG, int version) throws Exception
	{
		PlayerController playerController = new PlayerController();
		return playerController.getParametersRequired(idStudent, idSG, version);
	}
	
	public int startGamePlay(int idSG, int version, int idStudent, ArrayList<JSONObject> params) throws Exception
	{
		// use or create player 				-> idPlayer 
		PlayerController playerController = new PlayerController();
		int idPlayer = playerController.getPlayerFromIdStudent(idStudent, idSG, version);
		if (idPlayer == 0)
		{
			// create a player
			idPlayer = playerController.createPlayer(idSG, version, idStudent, params);
		}
		if (idPlayer <= 0)
		{
			return -1;
		}
		
		// create row in gameplay table			-> idGameplay
		GamePlayController gpController = new GamePlayController();
		int idGamePlay = gpController.startGamePlay(idPlayer, idSG, version);
				
		return idGamePlay;
	}
	
	public ArrayList<JSONObject> assess(int idGamePlay, JSONObject action) throws Exception
	{
		GamePlayController gpController = new GamePlayController();
		return gpController.assess(idGamePlay, action);
	}
	
	public ArrayList<JSONObject> getScores(int idGamePlay) throws Exception
	{
		GamePlayController gpController = new GamePlayController();
		return gpController.getScores(idGamePlay);		
	}
	
	public float getScore(int idGamePlay, int idOutcome) throws Exception
	{
		GamePlayController gpController = new GamePlayController();
		return gpController.getScore(idGamePlay, idOutcome);
	}
	
	public ArrayList<JSONObject> getFeedback(int idGamePlay) throws Exception
	{
		FeedbackTriggerController feedbackTriggerController = new FeedbackTriggerController();
		return feedbackTriggerController.getFeedback(idGamePlay);
	}
	
	public JSONObject getLog(int idGamePlay) throws Exception
	{
		JSONObject logs = new JSONObject();
		
		// add action logs
		ActionLogController actionLogController = new ActionLogController();
		ArrayList<JSONObject> actionLogs = actionLogController.getActionLog(idGamePlay);
		logs.put("actionLog", actionLogs);
		
		// add feedback logs
		FeedbackLogController feedbackLogController = new FeedbackLogController();
		ArrayList<JSONObject> feedbackLogs = feedbackLogController.getFeedbackLog(idGamePlay);
		logs.put("feedbackLog", feedbackLogs);
		
		return logs;
	}
	
	public int endGamePlay(int idGamePlay) throws Exception
	{
		GamePlayController gpController = new GamePlayController();
		return gpController.endGamePlay(idGamePlay);	
	}
	
	// ********************** Private functions ********************** //

 	private JSONObject DSLtoJSON(Model model)
	{
		JSONObject json = new JSONObject();
		
		// ##################### SERIOUS GAME ######################	
		GameDescription sg = model.getGame();
		JSONObject sgJson =  new JSONObject();

		sgJson.put("name", sg.getName());
		sgJson.put("idDeveloper", sg.getDev());
		if (sg.getDesc() != null) { sgJson.put("description", sg.getDesc()); }
		if (sg.getAgeRange() != null) { 
			sgJson.put("ageMin", new Integer(sg.getAgeRange().getAgeMin()));
			sgJson.put("ageMax", new Integer(sg.getAgeRange().getAgeMax()));
		}
		if (sg.getLang() != null) { sgJson.put("lang", sg.getLang()); }
		if (sg.getCountry() != null) { sgJson.put("country", sg.getCountry()); }
		if (sg.getGenre() != null) { sgJson.put("genre", sg.getGenre()); }
		if (sg.getSubject() != null) { sgJson.put("subject", sg.getSubject()); }
		if (sg.getPublic() != null) { sgJson.put("public", sg.getPublic()); }
		
		json.put("seriousGame", sgJson);
		
		// ##################### PLAYER ######################	
		
		PlayerDescription player = model.getPlayer();
		if (player != null)
		{
			ArrayList<JSONObject> playerJson = new ArrayList<JSONObject>();
			
			for (Characteristic c : player.getCharacteristics()) {
				JSONObject characteristicJson = new JSONObject();
	
				characteristicJson.put("name", c.getName());
				
				String type;
				if (c.getType().getSimpleType() != null)
				{
					type = c.getType().getSimpleType() ;
				}
				else
				{
					type = "Enum (" + c.getType().getEnumType().getEnumValues().get(0);
					for (int i = 1 ; i < c.getType().getEnumType().getEnumValues().size() ; i++)
					{
						type += ", " + c.getType().getEnumType().getEnumValues().get(i);
					}
					type += ")";
				}
				
				characteristicJson.put("type", type);
							
				playerJson.add(characteristicJson);
			}
			
			json.put("player", playerJson);
		}

		// ##################### LEARNING OUTCOME ######################
		
		LearningOutcomes los = model.getLearningOutcomes();
		JSONObject losJSON = new JSONObject();
		
		for (Outcome o : los.getOutcomes())
		{
			JSONObject outcomeJson = new JSONObject();
			
			if (o.getDesc() != null) { outcomeJson.put("desc", o.getDesc()); }
			if ((Integer)o.getValue() != null) { outcomeJson.put("value", o.getValue()); }
			if (o.getType() != null) { outcomeJson.put("type", o.getType()); }
			
			ArrayList<JSONObject> feedback = new ArrayList<JSONObject>();
			
			if (model.getFeedbackModel() != null)
			{
				for (Trigger t : model.getFeedbackModel().getTriggers())
				{
					if (t.getPerf() != null && t.getPerf().getOutcome() == o)
					{
						JSONObject f = new JSONObject();
						
						f.put("limit", new Integer(t.getPerf().getLimit()));
						f.put("sign", t.getPerf().getSign());
						ArrayList<String> feedbackToTrigger = new ArrayList<String>();
						
						for (TriggerFeedback reaction : t.getPerf().getTriggerReactions())
						{
							feedbackToTrigger.add(reaction.getFeedback().getName());
						}
						
						f.put("feedback", feedbackToTrigger);
						
						feedback.add(f);
					}
				}
			}
			
			outcomeJson.put("feedbackTriggered", feedback);
			
			
			losJSON.put(o.getName(), outcomeJson);
		}
		
		json.put("learningOutcomes", losJSON);
		
		// ##################### FEEDBACK ######################	
		
		FeedbackMessages fdbck = model.getFeedbackMessages();
		
		if (fdbck != null)
		{
			JSONObject fdbckJson = new JSONObject();
			
			for (Feedback f : fdbck.getFeedbackMsgs())
			{
				JSONObject fJson = new JSONObject();
				
				fJson.put("message", f.getMessage());
				if (f.getType() != null) { fJson.put("type", f.getType()); }
				if (f.isFinal()) { fJson.put("final", "true"); }
				
				fdbckJson.put(f.getName(), fJson);
			}
			
			json.put("feedback", fdbckJson);
		}
		
		// ##################### EVIDENCE MODEL ######################
		
		EvidenceModel actions = model.getEvidenceModel();
		JSONObject actionsJson = new JSONObject();
		
		for (Action a : actions.getActions())
		{
			// prepare the new JSON object
			String actionName = a.getName();
			JSONObject assessment = new JSONObject();
			
			// get the action parameters
			JSONObject paramsJson = new JSONObject();
			for (Parameter p : a.getParams())
			{
				String type;
				if (p.getType().getSimpleType() != null)
				{
					type = p.getType().getSimpleType() ;
				}
				else
				{
					type = "Enum (" + p.getType().getEnumType().getEnumValues().get(0);
					for (int i = 1 ; i < p.getType().getEnumType().getEnumValues().size() ; i++)
					{
						type += ", " + p.getType().getEnumType().getEnumValues().get(i);
					}
					type += ")";
				}
				paramsJson.put(p.getName(), type);
			}
			assessment.put("params", paramsJson);
			
			// get the reactions
			ArrayList<JSONObject> reactions = new ArrayList<JSONObject>();
			
			for (Points assess : a.getPoints())
			{
				JSONObject assessJson = new JSONObject();				
				if (assess.isOthers())
				{
					assessJson.put("else", "true");
				}
				else
				{
					ArrayList<JSONObject> valuesJson = new ArrayList<JSONObject>();
					for (Params p : assess.getParams())
					{			
						JSONObject paramValuesJson = new JSONObject();
						for (int i = 0 ; i < p.getValues().size() ; i++)
						{
							if (a.getParams().get(i).getType().equals("Int"))
							{
								paramValuesJson.put(a.getParams().get(i).getName(), 
										new Integer(p.getValues().get(i)));
							}
							else if (a.getParams().get(i).getType().equals("Float"))
							{
								paramValuesJson.put(a.getParams().get(i).getName(), 
										new Float(p.getValues().get(i)));
							}
							else
							{
								paramValuesJson.put(a.getParams().get(i).getName(), 
										p.getValues().get(i));
							}							
						}
						valuesJson.add(paramValuesJson);
					}
					assessJson.put("values", valuesJson);
				}
				
				JSONObject markJson = new JSONObject();
				if (assess.getOutcome() != null)
				{
					markJson.put("learningOutcome", assess.getOutcome().getName());
				}
				else
				{
					markJson.put("learningOutcome", model.getLearningOutcomes().getOutcomes().get(0).getName());					
				}
				if (assess.getPts().isNegative())
				{
					markJson.put("mark", new Integer(assess.getPts().getValue()*-1));
				}
				else
				{
					markJson.put("mark", new Integer(assess.getPts().getValue()));
				}
				assessJson.put("mark", markJson);
				
				if (a.getReactions() != null)
				{
					ArrayList<String> feedbackJson = new ArrayList<String>();
					
					for (Reaction r : a.getReactions().getReaction())
					{
						if (r.getPointsC() != null && r.getPointsC().getKeyWd().equals("any"))
						{
							if (r.getPointsC().getSign() != null && 
									((r.getPointsC().getSign().equals("+") && !assess.getPts().isNegative())
									 || (r.getPointsC().getSign().equals("-") && assess.getPts().isNegative())))
							{
								feedbackJson.add(r.getFeedback().getName());
							}
							else if (r.getPointsC().getValue() != null &&
										r.getPointsC().getValue().equals(assess.getPts()))
							{
								feedbackJson.add(r.getFeedback().getName());								
							}
						}
					}					
					assessJson.put("feedback", feedbackJson);
				}
				reactions.add(assessJson);
			}
			assessment.put("reactions", reactions);
			
			actionsJson.put(actionName, assessment);
		}
		
		json.put("evidenceModel", actionsJson);
		
		// ##################### INACTIVITY FEEDBACK ######################
		
		FeedbackModel feedbackModel = model.getFeedbackModel();
		
		
		if (feedbackModel != null)
		{
			ArrayList<JSONObject> inactivityJson = new ArrayList<JSONObject>();
		
			for (Trigger i : feedbackModel.getTriggers())
			{
				if (i.getInactivity() != null)
				{
					JSONObject inactivity = new JSONObject();

					inactivity.put("limit", new Integer(i.getInactivity().getLimit()));
					inactivity.put("sign", i.getInactivity().getSign());
					ArrayList<String> feedbackToTrigger = new ArrayList<String>();
					ArrayList<JSONObject> marks = new ArrayList<JSONObject>();
					
					for (ActionReaction reaction : i.getInactivity().getTriggerReactions())
					{
						if (reaction.getTriggerFeedback() != null)
						{
							feedbackToTrigger.add(reaction.getTriggerFeedback().getFeedback().getName());
						}
						if (reaction.getUpdateScore() != null)
						{
							JSONObject mark = new JSONObject();
							Point point = reaction.getUpdateScore().getPts();
							int score = (point.isNegative())? point.getValue() * -1 : point.getValue();
							mark.put("mark", score);
							mark.put("learningOutcome", reaction.getUpdateScore().getOutcome().getName());
							
							marks.add(mark);
						}
					}
					if (feedbackToTrigger.size() > 0)
						inactivity.put("feedback", feedbackToTrigger);
					if (marks.size() > 0)
						inactivity.put("mark", marks);
					
					inactivityJson.add(inactivity);
				}
			}
			json.put("inactivityFeedback", inactivityJson);
		}
		
		// ##################### INACTIVITY FEEDBACK ######################
		
		TimerActions timerActions = model.getEvidenceModel().getTimerActions();
		
		if (timerActions != null)
		{
			JSONObject actionTimers = new JSONObject();
			
			for (TimerAction t : timerActions.getTimerActions())
			{
				//JSONObject timer = new JSONObject();				
				ArrayList<JSONObject> timers = new ArrayList<JSONObject>();
				
				for (Timing timing : t.getTimings())
				{
					if (timing.isAfter())
					{
						JSONObject timer = new JSONObject();
						timer.put("limit", timing.getLimit());
						
						ArrayList<JSONObject> marks = new ArrayList<JSONObject>();
						ArrayList<String> feedback = new ArrayList<String>();
						
						for (ActionReaction reaction : timing.getTimingReactions()) {
							if (reaction.getTriggerFeedback() != null)
							{
								feedback.add(reaction.getTriggerFeedback().getFeedback().getName());
							}
							if (reaction.getUpdateScore() != null)
							{
								JSONObject mark = new JSONObject();
								Point point = reaction.getUpdateScore().getPts();
								int score = (point.isNegative())? point.getValue() * -1 : point.getValue();
								mark.put("mark", score);
								mark.put("learningOutcome", reaction.getUpdateScore().getOutcome().getName());
								
								marks.add(mark);
							}
						}
						timer.put("marks", marks);
						timer.put("feedback", feedback);
						
						timers.add(timer);
					}
				}
				actionTimers.put(t.getName(), timers);
			}
			
			json.put("timerActions", actionTimers);
		}
		
		return json;
	}

	private static String readFile( String file ) throws IOException {
	    @SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}

	
}
