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

import java.util.Random;

/**
 * Created by yarvis on 07/10/15.
 */
public class MySurfaceServerView extends SurfaceView implements SurfaceHolder.Callback {

    Random rand = new Random();
    /*Mando*/
    Bitmap buttonA, buttonB, buttonUp, buttonDown, buttonRight, buttonLeft;
    Bitmap bitmapAsteroid;
    public int level;
    /*---FIN--*/

    Bitmap bitmapMissil;
    Game game;
    private Bitmap scaled;

    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint4 = new Paint(Paint.ANTI_ALIAS_FLAG);

    int xx = 100;
    int yy = 150;


    int idClient = 1;

    TCPServer tcpServer;
    TCPClient tcpClient;

    private MainThread thread;

    Bitmap shipsDrawOn[] , shipsDrawOff[];

    public boolean playingVisible, playing;

    Context context;

    float dpx,dpy,mdpx,mdpy, adpx;

    public MySurfaceServerView(Context context, TCPServer tcpServer) {

        super(context);
        this.context =context;
        this.level = 1;
        //add callback to the surface holder (to detect press button (events) )
        getHolder().addCallback(this);

        shipsDrawOff = new Bitmap[3];
        shipsDrawOn = new Bitmap[3];

        game = new Game();


        thread = new MainThread(getHolder(), this);

        this.tcpServer = tcpServer;

        this.tcpServer.setSurface(this);
        playingVisible=true;
        playing=true;

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

        bitmapAsteroid = BitmapFactory.decodeResource(getResources(),
                R.drawable.asteroide);

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

        paint4.setColor(Color.GREEN);
        paint4.setTextSize(22);
        paint4.setTypeface(Typeface.create(paint4.getTypeface(), Typeface.BOLD));
        paint4.setStyle(Paint.Style.FILL);


        dpx = convertPixelsToDp(43.0f,context)/2.0f;
        dpy = convertPixelsToDp(40.0f,context)/2.0f;

        mdpx = convertPixelsToDp(22.0f,context)/2.0f;
        mdpy = convertPixelsToDp(6.0f,context)/2.0f;
        adpx = convertPixelsToDp(27.0f,context)/2.0f;
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
        //Log.e("WX, WY: ", ""+getWidth()+" "+getHeight());
        game.setWXY(getWidth(), getHeight() );
        game.addSpaceship(0);
        thread.setRunning(true);
        thread.start();

    }

    public void updateToClients(){
        if(tcpServer!=null){
            tcpServer.sendMessage(game);

        }
        else Log.e("TCP","tcp NULL");
    }

    public void updateToClients(int id){
        if(tcpServer!=null){
            tcpServer.sendMessage(game,id);

        }
        else Log.e("TCP","tcp NULL");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!playingVisible) return true;
        int wx = getWidth();
        int wy = getHeight();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateState(event.getX(), event.getY(), wx, wy, 0);

                break;
            case MotionEvent.ACTION_UP:
                updateState(event.getX(), event.getY(), wx, wy,false, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                updateState(event.getX(), event.getY(), wx, wy,false, 0);
                break;
        }

        return true;
    }

    public void updateState(float x,float y,int wx,int wy, int id){
        //game.setLevel(level);
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            game.rotateRight(id);
            updateToClients();
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            game.rotateLeft(id);
            updateToClients();
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            game.rotateRight(id);
            updateToClients();
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            game.rotateLeft(id);
            updateToClients();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            game.turbo(id);
            game.noRotate(id);
            updateToClients();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            game.shot(id);
            updateToClients();
        }

        /*NOAMLR ASTEROID*/
        if( rand.nextInt(100)<5*game.getLevel() ){
            game.shotAsteroid(game.getLevel());
            updateToClients();
        }
        /*FIN: ASTEROID*/

    }

    public void updateState(float x,float y,int wx,int wy,boolean stop, int id){
        //game.setLevel(level);
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            game.noRotate(id);
            updateToClients();
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            game.noRotate(id);
            updateToClients();
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            game.noRotate(id);
            updateToClients();
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            game.noRotate(id);
            updateToClients();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            game.noRotate(id);
            updateToClients();
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            updateToClients();
        }

        /*NOAMLR ASTEROID*/
        if( rand.nextInt(100)<5*game.getLevel() ){
            game.shotAsteroid(game.getLevel());
            updateToClients();
        }
        /*FIN: ASTEROID*/

    }

    public void update(){
        game.update(getWidth(), getHeight(),dpx,mdpx );
        playingVisible = game.getShips().get(0).getLiveVisible();
        playing = game.getShips().get(0).getLive();
    } /*noamlr*/


    public void doDraw(Canvas canvas) { /*noamlr se le agregó el id*/

        int wx = getWidth();
        int wy = getHeight();

        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);

        scaled = Bitmap.createScaledBitmap(background, wx, wy, true);

        canvas.drawBitmap(scaled, 0, 0, null);

        canvas.drawText("Nivel "+game.getLevel(), 30,30,paint4);
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
                if(cntPlaying==1) {
                    canvas.drawText("Ganaste!", 100, 100, paint3);
                    if(game.getLevel()<6) {
                        game.setLevel(game.getLevel() + 1);
                        for(Spaceship tmpShip : game.getShips()){
                            tmpShip.setLive(true);
                            tmpShip.setLiveVisible(true);
                        }
                    }
                }
            }
        }else{
            canvas.drawText("Te reventaron!", 100,wy-100,paint3);
        }

        dpx = convertPixelsToDp(43.0f,context)/2.0f;
        dpy = convertPixelsToDp(40.0f,context)/2.0f;
        //Log.e("ERR",""+dpx+" "+dpy);
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

            }else{
                Matrix matrix = new Matrix();

                matrix.postRotate(ship.getAngle(), dpx, dpy); /*noamlr*/
                matrix.postTranslate((float) (ship.getPosX() - dpx), ship.getPosY() - dpy); //noamlr
                canvas.drawBitmap(shipsDrawOff[cnt], matrix, null);
                //canvas.drawCircle(ship.getPosX(), ship.getPosY(), 10, paint3);
            }
        }


        mdpx = convertPixelsToDp(22.0f,context)/2.0f;
        mdpy = convertPixelsToDp(6.0f, context) / 2.0f;

        adpx = convertPixelsToDp(37.0f, context) /2.0f;

        for(Missil tmpmissil : game.getMissil()) {
            if (!tmpmissil.getActive()) continue;
            Matrix matrix = new Matrix();
            matrix.postRotate(tmpmissil.getAnglePosition(), mdpx, mdpy);
            matrix.postTranslate(tmpmissil.getPosX() - mdpx, tmpmissil.getPosY() - mdpy);
            canvas.drawBitmap(bitmapMissil, matrix, null);
            //canvas.drawCircle(tmpmissil.getPosX(), tmpmissil.getPosY(), 6, paint3);
        }

        for(Asteroid tmpasteroid : game.getAsteroids()) {
            if (!tmpasteroid.getActive()) continue;
            Matrix matrix = new Matrix();
            matrix.postRotate(tmpasteroid.getAnglePosition(), adpx, adpx);
            matrix.postTranslate(tmpasteroid.getPosX() - adpx, tmpasteroid.getPosY() - adpx);
            canvas.drawBitmap(bitmapAsteroid, matrix, null);
            //canvas.drawCircle(tmpmissil.getPosX(), tmpmissil.getPosY(), 6, paint3);
        }
    }


    public synchronized void setGame(Game tmp,int id){
        game=tmp;
        updateToClients(id);
    }

    public synchronized void setGame(Game tmp){
        game=tmp;
    }

    public Game getGame(){
        return game;
    }


    public static float convertPixelsToDp(float px, Context context){
        return px * context.getResources().getDisplayMetrics().density;
    }

}

