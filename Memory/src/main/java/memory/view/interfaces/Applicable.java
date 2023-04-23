package memory.view.interfaces;

import javafx.application.Application;

/*
 * Cette Interface sert à préciser une Application pour chaque classe qui aurait besoin de son application principale.
 * Elle sert donc à montrer qu'une classe utilise son application principale dans son code.
 */

public interface Applicable {
	
	void setApplication(Application app);

}
