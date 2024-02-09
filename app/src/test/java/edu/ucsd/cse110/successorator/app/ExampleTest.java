//package edu.ucsd.cse110.successorator.app;
//
//import static org.junit.Assert.assertEquals;
//
//import android.content.Context;
//import androidx.room.Room;
//import androidx.test.core.app.ApplicationProvider;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.List;
//
//import edu.ucsd.cse110.successorator.app.data.db.GoalEntity;
//import edu.ucsd.cse110.successorator.app.data.db.GoalsDao;
//import edu.ucsd.cse110.successorator.app.data.db.SuccessoratorDatabase;
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
// */
//public class ExampleTest {
//    private GoalsDao goalsDao;
//    private SuccessoratorDatabase db;
//
////    @Before
////    public void createDb() {
////        Context context = ApplicationProvider.getApplicationContext();
////        db = Room.inMemoryDatabaseBuilder(context, SuccessoratorDatabase.class).build();
////        goalsDao = db.goalsDao();
////    }
////
////    @After
////    public void closeDb() throws IOException {
////        db.close();
////    }
//
//    @Test
//    public void writeUserAndReadInList() throws Exception {
//        assertEquals(1, 1);
////        GoalEntity goalEntity = new GoalEntity("goal1", 1);
////        List<GoalEntity> allGoals = goalsDao.findAll();
////        assertEquals(allGoals.size(), 1);
//    }
//}

package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}