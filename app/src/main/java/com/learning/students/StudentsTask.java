package com.learning.students;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StudentsTask extends AsyncTask<File, Void, List<Student>> {

    // This is how to notify whoever called this activity without knowing who called it.
    // We create our own interface. Do it like this.
    private ResultCallBack resultCallBack;

    interface ResultCallBack {
        void onStudentsReady(List<Student> students);
    }
    StudentsTask(ResultCallBack resultCallBack) {
        this.resultCallBack = resultCallBack;
    }
    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.
     * To better support testing frameworks, it is recommended that this be
     * written to tolerate direct execution as part of the execute() call.
     * The default version does nothing.</p>
     *
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param students The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    // UI Thread
    @Override
    protected void onPostExecute(List<Student> students) {
        resultCallBack.onStudentsReady(students);

    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This will normally run on a background thread. But to better
     * support testing frameworks, it is recommended that this also tolerates
     * direct execution on the foreground thread, as part of the {@link #execute} call.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param files The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    // Background thread
    @Override
    protected List<Student> doInBackground(File... files) {
        File studentsFile = files[0];
        try {
            List<Student> localThreadStudentsFromFile;
            if (!studentsFile.exists()) {
                localThreadStudentsFromFile = Student.generate();
                studentsFile.createNewFile();
                Student.saveStudents(localThreadStudentsFromFile, studentsFile);
            } else {
                localThreadStudentsFromFile = Student.loadStudents(studentsFile);
            }
            return localThreadStudentsFromFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
