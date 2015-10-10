package com.example.jeslev.concurrentgamenetwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yarvis on 07/10/15.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    Bitmap bitmapMissil;
    Game game;
    private Bitmap scaled;

    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintServer = new Paint(Paint.ANTI_ALIAS_FLAG);
    int xx = 100;
    int yy = 150;

    public boolean playing;

    private MainThread thread;
    TCPClient tcpClient;
    int idClient = 1;

    float dpx, dpy,mdpx,mdpy;

    Context context;
    Bitmap shipsDrawOn[] , shipsDrawOff[];
    public MySurfaceView(Context context, TCPClient tcpClient) {

        super(context);
        this.context =context;
        //add callback to the surface holder (to detect press button (events) )
        getHolder().addCallback(this);

        shipsDrawOff = new Bitmap[3];
        shipsDrawOn = new Bitmap[3];

        this.tcpClient = tcpClient;
        this.tcpClient.setSurface(this);
        //focusable to get touch events
        idClient = this.tcpClient.getContainer().getID();
        game = this.tcpClient.getContainer().getGame();

        //game.addSpaceship(0);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        shipsDrawOn[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship2);

        shipsDrawOn[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship4);

        shipsDrawOn[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship4);

        shipsDrawOff[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship);

        shipsDrawOff[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship3);

        shipsDrawOff[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship3);

        bitmapMissil = BitmapFactory.decodeResource(getResources(),
                R.drawable.misil);

        paint2.setColor(Color.BLUE);
        paint2.setStyle(Paint.Style.FILL);

        paint3.setColor(Color.GREEN);
        paint3.setTextSize(80);
        paint3.setTypeface(Typeface.create(paint3.getTypeface(), Typeface.BOLD));
        paint3.setStyle(Paint.Style.FILL);


        paintServer.setColor(Color.GRAY);
        paintServer.setStyle(Paint.Style.FILL);

        playing=true;

        dpx = convertPixelsToDp(43.0f,context)/2.0f;
        dpy = convertPixelsToDp(40.0f,context)/2.0f;

        mdpx = convertPixelsToDp(22.0f,context)/2.0f;
        mdpy = convertPixelsToDp(6.0f,context)/2.0f;
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
        game.setWXY(getWidth(), getHeight() );
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

    public void updateToServer(){
        if(tcpClient!=null){
            tcpClient.sendMessage(game);

        }
        else Log.e("TCP", "tcp NULL");

    }

    public synchronized void setGame(Game tmp){
        game=tmp;
        //updateToServer();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!playing) return true;

        int wx = getWidth();
        int wy = getHeight();

        //ServidorEnviarCoordenadas();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateState(event.getX(), event.getY(), wx, wy, this.idClient);
                //if(game.getTurbo())
                //    doDraw(getHolder(),bitmapOn, (int)event.getX(), (int)event.getY(),1);
                //else
                //    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                //Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                updateState(event.getX(), event.getY(), wx, wy,false, this.idClient);
            //    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
            //Log.d("TouchEvent", "getAction()" + "ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
                //updateState(event.getX(), event.getY(), wx, wy);
                //doDraw(getHolder(),bitmapOff  , (int)event.getX(), (int)event.getY(),1);
                break;
            case MotionEvent.ACTION_CANCEL:
                updateState(event.getX(), event.getY(), wx, wy,false, this.idClient);
            //Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
            //     doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                break;
        }



        return true;
    }

    public void updateState(float x,float y,int wx,int wy, int id){
        //System.out.println("TouchEvent111"+"X:" + event.getX() + ",Y:" + event.getY());
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click ARRIBA", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ARRIBA");
            game.rotateRight(id);
            updateToServer();
            //yy = yy - 5;
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ABAJO");
            game.rotateLeft(id);
            updateToServer();
            //yy = yy  + 5;
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click IZQUIERDA");
            game.rotateRight(id);
            updateToServer();
            //xx = xx  - 5;
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click DERECHA");
            game.rotateLeft(id);
            updateToServer();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click A");
            game.turbo(id);
            game.noRotate(id);
            updateToServer();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click B");
            //xx = xx  + 5;

            game.shot(id);
            updateToServer();
        }

    }

    public void updateState(float x,float y,int wx,int wy,boolean stop, int id){
        //System.out.println("TouchEvent111"+"X:" + event.getX() + ",Y:" + event.getY());
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click ARRIBA", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ARRIBA");
            game.noRotate(id);
            updateToServer();
            //yy = yy - 5;
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ABAJO");
            game.noRotate(id);
            updateToServer();
            //yy = yy  + 5;
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click IZQUIERDA");
            game.noRotate(id);
            updateToServer();
            //xx = xx  - 5;
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click DERECHA");
            game.noRotate(id);
            updateToServer();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click A");
            //game.turbo(0);
            game.noRotate(id);
            updateToServer();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click B");
            //xx = xx  + 5;
        }

    }
    // Surfaceが作られた時呼び出される


    public void update(){
        game.update(getWidth(), getHeight(),dpx,mdpx);
        playing = game.getShips().get(idClient).getLive();
    };


//    public void repintar(){
//
////			xx = xx__;
////			yy = yy__;
//       // doDraw(getHolder(),bitmapOff, 66, 66,0);
//
//
//    }

    public void doDraw(Canvas canvas) { /*noamlr se le agregó el id*/

        //Log.e("draw","init draw "+id );
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
        //Log.e("draw", "draw bg " + id);
        //XYPosition pos = null;

        //if (flat == 1){
        //    pos = imageXYposition(canvas, bitmap, new XYPosition(centerX,centerY));
        //}
        //System.out.println("wx: " + wx + ",wy: "+wy);

        if(playing) {
            canvas.drawCircle(100, wy - 150, 30, paint2); // up arrow

            canvas.drawCircle(100, wy - 50, 30, paint2); // down arrow

            canvas.drawCircle(50, wy - 100, 30, paint2); // left arrow

            canvas.drawCircle(150, wy - 100, 30, paint2); // right arrow

            canvas.drawCircle(wx - 150, wy - 150, 30, paint2); // a button

            canvas.drawCircle(wx - 150, wy - 50, 30, paint2); // b button
        }else{
            canvas.drawText("Game Over!", 100,wy-100,paint3);
        }
        //canvas.drawRect(xx, yy, xx+10, yy+10, paint3);
        //canvas.drawText("Envio FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF  x=" + xx + " y=" + yy, 50, 10,paintServer);

        //if (flat == 1){
        //    canvas.drawBitmap(bitmap, pos.getX(), pos.getY(), null);
        //}

        dpx = convertPixelsToDp(43.0f,context)/2.0f;
        dpy = convertPixelsToDp(40.0f,context)/2.0f;

        int cnt = -1;
        for(Spaceship ship : game.getShips()){
            cnt++;
            if(!ship.getLive()) continue;
            if(ship.getTurbo()){ //noamlr
                Matrix matrix = new Matrix();
                //matrix.postRotate((float)ship.getAngle());
                //matrix.postRotate(game.getShip().getAngle(), 14, 19);
                //matrix.postTranslate(game.getShip().getPosX(), game.getShip().getPosY());

                matrix.postRotate(ship.getAngle(), dpx, dpy); //noamlr
                matrix.postTranslate((float) (ship.getPosX() - dpx), ship.getPosY() - dpy); //noamlr

                canvas.drawBitmap(shipsDrawOn[cnt], matrix, null);
                canvas.drawCircle(ship.getPosX(), ship.getPosY(), 10, paint3);
                //System.err.println(ship.getPosX() + " " + ship.getPosY() + " " + ship.getAngle());

            }else{
                Matrix matrix = new Matrix();
                //matrix.postRotate((float)ship.getAngle());
                //matrix.postRotate(game.getShip().getAngle(), 14, 19);
                //matrix.postTranslate(game.getShip().getPosX(), game.getShip().getPosY());

                matrix.postRotate(ship.getAngle(), dpx, dpy); /*noamlr*/
                matrix.postTranslate((float) (ship.getPosX() - dpx), ship.getPosY() - dpy); //noamlr
                canvas.drawBitmap(shipsDrawOff[cnt], matrix, null);
                canvas.drawCircle(ship.getPosX(), ship.getPosY(), 10, paint3);
                //System.err.println(ship.getPosX() + " " + ship.getPosY() + " " + ship.getAngle())
            }
        }


        mdpx = convertPixelsToDp(22.0f,context)/2.0f;
        mdpy = convertPixelsToDp(6.0f,context)/2.0f;

        for(Missil tmpmissil : game.getMissil()){
            if(!tmpmissil.getActive()) continue;
            Matrix matrix = new Matrix();
            matrix.postRotate(tmpmissil.getAnglePosition(), mdpx, mdpy);
            matrix.postTranslate(tmpmissil.getPosX() - mdpx, tmpmissil.getPosY() - mdpy);
            canvas.drawBitmap(bitmapMissil, matrix, null);
            canvas.drawCircle(tmpmissil.getPosX(), tmpmissil.getPosY(), 6, paint3);
        }
    }




    public Game getGame(){
        return game;
    }

    public static float convertPixelsToDp(float px, Context context){
        return px * context.getResources().getDisplayMetrics().density;
    }

}

