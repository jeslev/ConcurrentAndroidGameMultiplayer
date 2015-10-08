package com.example.jeslev.concurrentgamenetwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yarvis on 07/10/15.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    Bitmap bitmapOff;
    Bitmap bitmapOn;
    Game game;
    private Bitmap scaled;

    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintServer = new Paint(Paint.ANTI_ALIAS_FLAG);
    int xx = 100;
    int yy = 150;

    private MainThread thread;

    public MySurfaceView(Context context) {

        super(context);
        //add callback to the surface holder (to detect press button (events) )
        getHolder().addCallback(this);

        game = new Game();
        thread = new MainThread(getHolder(), this);

        //focusable to get touch events
        setFocusable(true);

        bitmapOff = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship);
        bitmapOn = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship2);//////////////////

        paint2.setColor(Color.BLUE);
        paint2.setStyle(Paint.Style.FILL);

        paint3.setColor(Color.GREEN);
        paint3.setStyle(Paint.Style.FILL);


        paintServer.setColor(Color.GRAY);
        paintServer.setStyle(Paint.Style.FILL);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry=true;
        while(retry){
            try{ thread.setRunning(false); thread.join();}
            catch(Exception e) {}
            retry=false;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//			Canvas canvas = holder.lockCanvas();
//			canvas.drawColor(Color.GRAY);
//			canvas.drawCircle(100, 150, 30, paint2);

        //start the game loop
        thread.setRunning(true);
        thread.start();

        //int x = 100;
        //int y = 100;
        //doDraw(holder,bitmapOff, x, y,1);
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

        //ServidorEnviarCoordenadas();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateState(event.getX(), event.getY(), wx, wy);
                //if(game.getTurbo())
                //    doDraw(getHolder(),bitmapOn, (int)event.getX(), (int)event.getY(),1);
                //else
                //    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                //Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                updateState(event.getX(), event.getY(), wx, wy,false);
            //    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
            //Log.d("TouchEvent", "getAction()" + "ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
                //updateState(event.getX(), event.getY(), wx, wy);
                //doDraw(getHolder(),bitmapOff  , (int)event.getX(), (int)event.getY(),1);
                break;
            case MotionEvent.ACTION_CANCEL:
                updateState(event.getX(), event.getY(), wx, wy,false);
            //Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
            //     doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                break;
        }



        return true;
    }

    public void updateState(float x,float y,int wx,int wy){
        //System.out.println("TouchEvent111"+"X:" + event.getX() + ",Y:" + event.getY());
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click ARRIBA", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ARRIBA");
            game.rotateRight(0);
            //yy = yy - 5;
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ABAJO");
            game.rotateLeft(0);
            //yy = yy  + 5;
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click IZQUIERDA");
            game.rotateRight(0);
            //xx = xx  - 5;
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click DERECHA");
            game.rotateLeft(0);
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click A");
            game.turbo(0);
            game.noRotate(0);
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click B");
            //xx = xx  + 5;
        }

    }

    public void updateState(float x,float y,int wx,int wy,boolean stop){
        //System.out.println("TouchEvent111"+"X:" + event.getX() + ",Y:" + event.getY());
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click ARRIBA", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ARRIBA");
            game.noRotate(0);
            //yy = yy - 5;
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ABAJO");
            game.noRotate(0);
            //yy = yy  + 5;
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click IZQUIERDA");
            game.noRotate(0);
            //xx = xx  - 5;
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click DERECHA");
            game.noRotate(0);
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click A");
            //game.turbo(0);
            game.noRotate(0);
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click B");
            //xx = xx  + 5;
        }

    }
    // Surfaceが作られた時呼び出される


    public void update(){ game.update(getWidth(), getHeight());};


//    public void repintar(){
//
////			xx = xx__;
////			yy = yy__;
//       // doDraw(getHolder(),bitmapOff, 66, 66,0);
//
//
//    }

    public void doDraw(Canvas canvas) {

        //canvas.drawColor(Color.GRAY);
        int wx = getWidth();
        int wy = getHeight();

        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        //scaled = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);
        scaled = Bitmap.createScaledBitmap(background, wx, wy, true);

        canvas.drawBitmap(scaled, 0, 0, null);
        //XYPosition pos = null;

        //if (flat == 1){
        //    pos = imageXYposition(canvas, bitmap, new XYPosition(centerX,centerY));
        //}
        //System.out.println("wx: " + wx + ",wy: "+wy);

        canvas.drawCircle(100, wy - 150, 30, paint2); // up arrow

        canvas.drawCircle(100, wy-50, 30, paint2); // down arrow

        canvas.drawCircle(50, wy-100, 30, paint2); // left arrow

        canvas.drawCircle(150, wy-100, 30, paint2); // right arrow

        canvas.drawCircle(wx-150, wy-150, 30, paint2); // a button

        canvas.drawCircle(wx-150, wy-50, 30, paint2); // b button

        //canvas.drawRect(xx, yy, xx+10, yy+10, paint3);
        //canvas.drawText("Envio FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF  x=" + xx + " y=" + yy, 50, 10,paintServer);

        //if (flat == 1){
        //    canvas.drawBitmap(bitmap, pos.getX(), pos.getY(), null);
        //}
        if(game.getTurbo())
            game.draw(bitmapOn,canvas,wx,wy);
        else
            game.draw(bitmapOff,canvas,wx,wy);

    }

//    private void doDraw(SurfaceHolder holder,Bitmap bitmap,int centerX, int centerY) {
//        Canvas canvas = holder.lockCanvas();
//
//        //canvas.drawColor(Color.GRAY);
//
//
//        canvas.drawColor(Color.BLACK);
//        //XYPosition pos = imageXYposition(canvas, bitmap, new XYPosition(centerX,centerY));
//
//        //canvas.drawCircle(50, 50, 25, paint2);
//
//        //canvas.drawCircle(50, 150, 25, paint2);
//
//        //canvas.drawCircle(25, 100, 25, paint2);
//
//        //canvas.drawCircle(75, 100, 25, paint2);
//
//        //canvas.drawRect(xx, yy, xx+10, yy+10, paint3);
//
//        //		canvas.drawText("Envio FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF  x=" + xx + " y=" + yy, 50, 10,paintServerEnvia);
//
//
//        //canvas.drawBitmap(bitmap, pos.getX(), pos.getY(), null);
//
//
//
//        holder.unlockCanvasAndPost(canvas);
//
//
//    }



}

