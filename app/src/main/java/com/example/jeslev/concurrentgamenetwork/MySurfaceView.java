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

    /*Mando*/
    Bitmap buttonA, buttonB, buttonUp, buttonDown, buttonRight, buttonLeft;
    /*---FIN--*/

    Bitmap bitmapMissil;
    Game game;
    private Bitmap scaled;

    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    int xx = 100;
    int yy = 150;

    public boolean playingVisible, playing;

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
        idClient = this.tcpClient.getContainer().getID();
        game = this.tcpClient.getContainer().getGame();

        //Log.e("Game Live: ", ""+idClient);
        game.getShip(idClient).setLiveVisible(true);

        thread = new MainThread(getHolder(), this);

        //focusable to get touch events
        setFocusable(true);

        shipsDrawOn[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship6);

        shipsDrawOn[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship4);

        shipsDrawOn[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship8);

        shipsDrawOff[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship5);

        shipsDrawOff[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship3);

        shipsDrawOff[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.spaceship7);

        bitmapMissil = BitmapFactory.decodeResource(getResources(),
                R.drawable.misil);

        /*Mando*/
        buttonA = BitmapFactory.decodeResource(getResources(),
                R.drawable.buttona);
        buttonB = BitmapFactory.decodeResource(getResources(),
                R.drawable.buttonb);
        buttonUp = BitmapFactory.decodeResource(getResources(),
                R.drawable.buttonup);
        buttonDown = BitmapFactory.decodeResource(getResources(),
                R.drawable.buttondown);
        buttonRight = BitmapFactory.decodeResource(getResources(),
                R.drawable.buttonright);
        buttonLeft = BitmapFactory.decodeResource(getResources(),
                R.drawable.buttonleft);
        /*---FIN--*/

        paint2.setColor(Color.BLUE);
        paint2.setStyle(Paint.Style.FILL);

        paint3.setColor(Color.GREEN);
        paint3.setTextSize(80);
        paint3.setTypeface(Typeface.create(paint3.getTypeface(), Typeface.BOLD));
        paint3.setStyle(Paint.Style.FILL);

        playingVisible=true;
        playing = true;

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
        //start the game loop
        game.setWXY(getWidth(), getHeight() );
        thread.setRunning(true);
        thread.start();
    }

    public void updateToServer(){
        if(tcpClient!=null){
            tcpClient.sendMessage(game);

        }
        else Log.e("TCP", "tcp NULL");

    }

    public synchronized void setGame(Game tmp){
        game=tmp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!playingVisible) return true;

        int wx = getWidth();
        int wy = getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateState(event.getX(), event.getY(), wx, wy, this.idClient);
                break;
            case MotionEvent.ACTION_UP:
                updateState(event.getX(), event.getY(), wx, wy,false, this.idClient);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                updateState(event.getX(), event.getY(), wx, wy,false, this.idClient);
                break;
        }



        return true;
    }

    public void updateState(float x,float y,int wx,int wy, int id){
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            game.rotateRight(id);
            updateToServer();
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            game.rotateLeft(id);
            updateToServer();
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            game.rotateRight(id);
            updateToServer();
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            game.rotateLeft(id);
            updateToServer();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            game.turbo(id);
            game.noRotate(id);
            updateToServer();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            game.shot(id);
            updateToServer();
        }

    }

    public void updateState(float x,float y,int wx,int wy,boolean stop, int id){
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            game.noRotate(id);
            updateToServer();
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            game.noRotate(id);
            updateToServer();
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            game.noRotate(id);
            updateToServer();
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            game.noRotate(id);
            updateToServer();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            game.noRotate(id);
            updateToServer();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
        }

    }


    public void update(){
        game.update(getWidth(), getHeight(),dpx,mdpx);
        playingVisible = game.getShips().get(idClient).getLiveVisible();
        playing = game.getShips().get(idClient).getLive();
    };


    public void doDraw(Canvas canvas) { /*noamlr se le agregó el id*/

        //Log.e("draw","init draw "+id );
        int wx = getWidth();
        int wy = getHeight();

        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);

        scaled = Bitmap.createScaledBitmap(background, wx, wy, true);

        canvas.drawBitmap(scaled, 0, 0, null);

        if(playingVisible && playing) {
            //canvas.drawCircle(100, wy - 150, 30, paint2); // up arrow
            canvas.drawBitmap(buttonUp, 70, wy - 180, null);

            //canvas.drawCircle(100, wy - 50, 30, paint2); // down arrow
            canvas.drawBitmap(buttonDown, 70, wy - 80, null);

            //canvas.drawCircle(50, wy - 100, 30, paint2); // left arrow
            canvas.drawBitmap(buttonLeft, 20, wy - 130, null);

            //canvas.drawCircle(150, wy - 100, 30, paint2); // right arrow
            canvas.drawBitmap(buttonRight, 120, wy - 130, null);

            //canvas.drawCircle(wx - 150, wy - 150, 30, paint2); // a button
            canvas.drawBitmap(buttonA, wx - 180, wy - 180, null);
            canvas.drawBitmap(buttonB, wx - 180, wy - 80, null);
            //canvas.drawCircle(wx - 150, wy - 50, 30, paint2); // b button

            if(game.getShips().size()>1){
                int cntPlaying = 0;
                for(Spaceship tmpShip : game.getShips()){
                    if(tmpShip.getLive()) cntPlaying++;
                }
                if(cntPlaying==1)   canvas.drawText("Ganaste!", 100,100,paint3);
            }
        }else{
            canvas.drawText("Te reventaron!", 100,wy-100,paint3);
        }


        dpx = convertPixelsToDp(43.0f,context)/2.0f;
        dpy = convertPixelsToDp(40.0f,context)/2.0f;

        int cnt = -1;
        for(Spaceship ship : game.getShips()){
            cnt++;
            if(!ship.getLiveVisible()) continue;
            if(ship.getTurbo()){ //noamlr
                Matrix matrix = new Matrix();

                matrix.postRotate(ship.getAngle(), dpx, dpy); //noamlr
                matrix.postTranslate((float) (ship.getPosX() - dpx), ship.getPosY() - dpy); //noamlr

                canvas.drawBitmap(shipsDrawOn[cnt], matrix, null);
                //canvas.drawCircle(ship.getPosX(), ship.getPosY(), 10, paint3);
                //System.err.println(ship.getPosX() + " " + ship.getPosY() + " " + ship.getAngle());

            }else{
                Matrix matrix = new Matrix();

                matrix.postRotate(ship.getAngle(), dpx, dpy); /*noamlr*/
                matrix.postTranslate((float) (ship.getPosX() - dpx), ship.getPosY() - dpy); //noamlr
                canvas.drawBitmap(shipsDrawOff[cnt], matrix, null);
                //canvas.drawCircle(ship.getPosX(), ship.getPosY(), 10, paint3);
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
            //canvas.drawCircle(tmpmissil.getPosX(), tmpmissil.getPosY(), 6, paint3);
        }
    }




    public Game getGame(){
        return game;
    }

    public static float convertPixelsToDp(float px, Context context){
        return px * context.getResources().getDisplayMetrics().density;
    }

}

