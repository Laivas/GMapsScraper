//package scrapperGuiHandlers;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.stage.FileChooser;
//import javafx.stage.FileChooser.ExtensionFilter;
//import lombok.Getter;
//import lombok.Setter;
//import scrapperGui.ScriptsPane;
//import scrapperModel.Script;
//
//@Setter
//@Getter
//public class ScriptsPaneHandler implements EventHandler<ActionEvent>{
//
//	private ScriptsPane scriptsPane;
//	
//	private Script script;
//	
//	private String moveFileTo = "/ScrappingApp/";
//	
//	public ScriptsPaneHandler(ScriptsPane scriptsPane) {
//		
//		this.scriptsPane = scriptsPane;
//		
//	}
//
//	@Override
//	public void handle(ActionEvent event) {
//		// TODO Auto-generated method stub
//		
//		if(event.getSource() == scriptsPane.getAddScriptButton()) {
//			
//			script = new Script();
//			
//			if(scriptsPane.getUploadFromField().getText() != null) {			
//				
//				String userHome = System.getProperty("user.home").replaceAll("\\\\", "/");
//				
//				try {
//					
//					File file = new File(scriptsPane.getUploadFromField().getText());
//					
//					System.out.println(Paths.get(userHome + moveFileTo).toString());
//					
//					System.out.println(Paths.get(scriptsPane.getUploadFromField().getText()).toString());
//					
//					Files.move(Paths.get(scriptsPane.getUploadFromField().getText()), Paths.get(userHome + moveFileTo + file.getName()));
//					
//					scriptsPane.getUploadFromField().setText("");
//					
//					script.setScriptName(file.getName());
//					
//					if(!entryExists(file.getName())) {
//					
//						scriptsPane.getTableView().getItems().add(script);
//					
//					}
//					
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//					
//					
//			}
//			
//			
//		}
//		
//		if (event.getSource() == scriptsPane.getUploadFromButton()) {
//
//			FileChooser fileChooser = new FileChooser();
//
//			fileChooser.getExtensionFilters().add(new ExtensionFilter("Java Files", "*.java"));
//
//			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//
//			File file = fileChooser.showOpenDialog(scriptsPane.getScene().getWindow());
//
//			if (file != null) {
//
//				if (event.getSource() == scriptsPane.getUploadFromButton()) {
//
//					scriptsPane.getUploadFromField().setText(file.getAbsolutePath());
//
//				}
//
//			}
//
//		}
//		
//		
//	}
//	
//	public boolean entryExists(String scriptName) {
//		
//		for(Script script : scriptsPane.getTableView().getItems()) {
//			
//			if(script.getScriptName().equals(scriptName)) {
//				
//				return true;
//				
//			}
//			
//		}
//		
//		return false;
//		
//	}
//	
//}
