package com.example.jeslev.concurrentgamenetwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ScenarioActivity extends AppCompatActivity {

    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintServer = new Paint(Paint.ANTI_ALIAS_FLAG);


    public SurfaceView SurfaceViewX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SurfaceViewX = new MySurfaceView(this);
        setContentView(SurfaceViewX);

        paint2.setColor(Color.GRAY);
        paint2.setStyle(Paint.Style.FILL);

        paint3.setColor(Color.GREEN);
        paint3.setStyle(Paint.Style.FILL);


        paintServer.setColor(Color.GRAY);
        paintServer.setStyle(Paint.Style.FILL);

    }


    public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        Bitmap bitmapOff;
        Bitmap bitmapOn;
        public MySurfaceView(Context context) {
            super(context);
            // ホルダーのコールバックに自身をセットすることで、準備だできた時点で呼び出される
            getHolder().addCallback(this);
            /*bitmapOff = BitmapFactory.decodeResource(getResources(),
                    R.drawable.dibujito01);
            bitmapOn = BitmapFactory.decodeResource(getResources(),
                    R.drawable.dibujito02);////////////////// */
        }

         /*public void ServidorEnviarCoordenadas(){
            if (mTcpServer != null) {
                //textmp = edt_mensaje.getText().toString();
                mTcpServer.sendMessage(xx + " "+yy);
            }
        }*/

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int wx = getWidth();
            int wy = getHeight();

            System.out.println("TouchEvent111"+"X:" + event.getX() + ",Y:" + event.getY());
            if( 70 < event.getX() && event.getX()<130 && (wy-280)< event.getY() && event.getY()<(wy-220) ){
                //Toast.makeText(getApplicationContext(), "click ARRIBA", Toast.LENGTH_SHORT).show();
                System.out.println(" Click ARRIBA");

                //yy = yy - 2;
            }

            if( 70 < event.getX() && event.getX()<130 && (wy-180)< event.getY() && event.getY()<(wy-120) ){
                //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
                System.out.println(" Click ABAJO");
                //yy = yy  + 2;
            }

            if( 20 < event.getX() && event.getX()<80 && (wy-230)< event.getY() && event.getY()<(wy-170) ){
                //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
                System.out.println(" Click IZQUIERDA");
                //xx = xx  - 2;
            }

            if( 120 < event.getX() && event.getX()<180 && (wy-230)< event.getY() && event.getY()<(wy-170) ){
                //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
                System.out.println(" Click DERECHA");
                //??xx = xx  + 2;
            }

            if( (wx-280) < event.getX() && event.getX()<(wx-220) && (wy-280)< event.getY() && event.getY()<(wy-220) ){
                //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
                System.out.println(" Click A");
                //??xx = xx  + 2;
            }

            if( (wx-280) < event.getX() && event.getX()<(wx-220) && (wy-180)< event.getY() && event.getY()<(wy-120) ){
                //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
                System.out.println(" Click B");
                //??xx = xx  + 2;
            }

            //ServidorEnviarCoordenadas();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    doDraw(getHolder(),bitmapOn, (int)event.getX(), (int)event.getY(),1);
                    //Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_UP:
                    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                    //Log.d("TouchEvent", "getAction()" + "ACTION_UP");
                    break;
                case MotionEvent.ACTION_MOVE:
                    //Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
                    doDraw(getHolder(),bitmapOn, (int)event.getX(), (int)event.getY(),1);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    //Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
                    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                    break;
            }



            return true;
        }
        // Surfaceが作られた時呼び出される

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
//			Canvas canvas = holder.lockCanvas();
//			canvas.drawColor(Color.GRAY);
//			canvas.drawCircle(100, 150, 30, paint2);

            int x = 100;
            int y = 100;
            doDraw(holder,bitmapOff, x, y,1);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }


        public void repintar(){

//			xx = xx__;
//			yy = yy__;
            doDraw(getHolder(),bitmapOff, 66, 66,0);


        }

        public void doDraw(SurfaceHolder holder,Bitmap bitmap,int centerX, int centerY,int flat) {

            Canvas canvas = holder.lockCanvas();
            //canvas.drawColor(Color.GRAY);
            int wx = getWidth();
            int wy = getHeight();
            canvas.drawColor(Color.BLACK);
            //XYPosition pos = null;

            //if (flat == 1){
            //    pos = imageXYposition(canvas, bitmap, new XYPosition(centerX,centerY));
            //}

            canvas.drawCircle(100, wy-250, 30, paint2); // up arrow

            canvas.drawCircle(100, wy-150, 30, paint2); // down arrow

            canvas.drawCircle(50, wy-200, 30, paint2); // left arrow

            canvas.drawCircle(150, wy-200, 30, paint2); // right arrow

            canvas.drawCircle(wx-250, wy-250, 30, paint2); // a button

            canvas.drawCircle(wx-250, wy-150, 30, paint2); // b button

            //canvas.drawRect(xx, yy, xx+10, yy+10, paint3);
            //canvas.drawText("Envio FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF  x=" + xx + " y=" + yy, 50, 10,paintServer);

            //if (flat == 1){
            //    canvas.drawBitmap(bitmap, pos.getX(), pos.getY(), null);
            //}
            holder.unlockCanvasAndPost(canvas);


        }

		/*private void doDraw(SurfaceHolder holder,Bitmap bitmap,int centerX, int centerY) {
			Canvas canvas = holder.lockCanvas();

			//canvas.drawColor(Color.GRAY);



			canvas.drawColor(Color.BLACK);
			XYPosition pos = imageXYposition(canvas, bitmap, new XYPosition(centerX,centerY));

			canvas.drawCircle(50, 50, 25, paint2);

			canvas.drawCircle(50, 150, 25, paint2);

			canvas.drawCircle(25, 100, 25, paint2);

			canvas.drawCircle(75, 100, 25, paint2);

			canvas.drawRect(xx, yy, xx+10, yy+10, paint3);

			canvas.drawText("Envio FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF  x=" + xx + " y=" + yy, 50, 10,paintServerEnvia);


			canvas.drawBitmap(bitmap, pos.getX(), pos.getY(), null);



			holder.unlockCanvasAndPost(canvas);


		}*/

//        private XYPosition imageXYposition(Canvas canvas, Bitmap bitmap,XYPosition position) {
//            int bitmapHeight = bitmap.getHeight();
//            int bitmapWidth = bitmap.getWidth();
//            int canvasWidth = canvas.getWidth();
//            int canvasHeight = canvas.getHeight();
////Log.d("TouchEvent", "bitmapHeight : " + bitmapHeight + ", bitmapWidth : "+bitmapWidth +", canvasWidth : "+canvasWidth+", canvasHeight : "+canvasHeight);
//            int x = position.getX() - bitmapWidth / 2;
//            if (x < 0) {
//                x = 0;
//            }
//            if (x > canvasWidth - bitmapWidth) {
//                x = canvasWidth - bitmapWidth;
//            }
//            int y = position.getY() - bitmapHeight / 2;
//            if (y < 0) {
//                y = 0;
//            }
//            if (y > canvasHeight - bitmapHeight) {
//                y = canvasHeight - bitmapHeight;
//            }
//            //Log.d("TouchEvent", "x : " + x + ", y : "+ y);
//            //System.out.println("TouchEvent"+ "x : " + x + ", y : "+ y);
//            return new XYPosition(x, y);
//
//
//
//
//
//        }

    }

}
