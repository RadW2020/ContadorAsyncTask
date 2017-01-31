package com.pmdm.ud3_ContadorTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Ud3_ContadorTask extends Activity {

	private int mSleep, mMax;
	private TextView textView;
	private CounterTask counterTask;

    // Creo la variable fuera del doInbackground para que la pueda leer también otros métodos
    int progress;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// cadencia de la cuenta
		mSleep = 500;
		// tope del contador
		mMax = 30;
		// marcador
		textView = (TextView) findViewById(R.id.textView);

	}

	/**
	 * cancela la cuenta
	 * 
	 * @param v
	 */
	public void stopCounter(View v) {
		/*************************************************************************/
		// si cuenta no ha sido cancelada ---- REALIZAR acciones apropiadas
        //Toast.makeText(v.getContext(), "pulsado stop", Toast.LENGTH_SHORT ).show();

		if  ( (counterTask != null) ){

            //Toast.makeText(v.getContext(), "not null", Toast.LENGTH_SHORT ).show();
            counterTask.cancel(true);
            String msg = "Tarea completada con " + progress + " números";
            Toast.makeText( Ud3_ContadorTask.this , msg , Toast.LENGTH_LONG ).show();


        }
			// la arranca con los parámetros indicados en execute()


	}

	/**
	 * arranca la cuenta
	 * 
	 * @param v
	 */
	public void startCounter(View v) throws  InterruptedException {
		// si cuenta no iniciada nunca, o cancelada , o terminada--- REALIZAR acciones apropiadas
		if (counterTask == null || counterTask.isCancelled() || (counterTask.getStatus() == AsyncTask.Status.FINISHED) ) {
			// la arranca con los parámetros indicados en execute()
			counterTask = new CounterTask();
			counterTask.execute(mSleep, mMax);
		}
	}

	/**
	 * clase AsyncTask (pool de varios hilos) para realizar la tarea en segundo plano
	 */
	private class CounterTask extends AsyncTask<Integer, Integer, Integer> {
		/**
		 * se inicia la tarea en segundo plano con los parámetros pasados a su
		 * método execute(), y se devuelve el resultado a onPostExecute()
		 */
		protected Integer doInBackground(Integer... intParam) {
			int sleep = intParam[0];
			int max = intParam[1];
			// reanuda el contador
			progress = 0;
			// por debajo del tope
			while (progress < max) {
				// si cuenta cancelada
				if (isCancelled())
					// sale sin finalizar
					return progress;
				// si cuenta en marcha
				else {
					// envía el nuevo valor proporcionado por doWork()
					publishProgress(progress = doWork(progress));
					// simula la cadencia
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}//fin while
			// cuenta finalizada
			return progress;
		}

		/**
		 * se reciben los valores de progreso enviados por publishProgress(), y
		 * se actualiza la interfaz con ellos
		 */
		protected void onProgressUpdate(Integer... progress) {
			// recoge el único valor enviado
			int progressStatus = progress[0];
			// actualiza la interfaz
			textView.setText("" + progressStatus);
		}

		/**
		 * se recibe el valor devuelto por doInBackground()
		 */
		protected void onPostExecute(Integer result) {
			/**********************************************************/
			// si cuenta completada --- REALIZAR acciones apropiadas String.valueOf(progress)
            String msg = "Tarea completada con " + result + " números";
            Toast.makeText( Ud3_ContadorTask.this , msg , Toast.LENGTH_LONG ).show();

        }


	}

	/**
	 * valor de progreso de la tarea
	 * 
	 * @param progress
	 * @return
	 */
	private int doWork(int progress) {
		// de uno en uno
		return 1 + progress;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



}
