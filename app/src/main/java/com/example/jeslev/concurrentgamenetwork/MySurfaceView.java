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

    TCPServer tcpServer;

    private MainThread thread;

    Bitmap shipsDrawOn[] , shipsDrawOff[];

    public boolean playingVisible, playing;

    Context context;

    float dpx,dpy,mdpx,mdpy;

    public MySurfaceView(Context context, TCPServer tcpServer) {

        super(context);
        this.context =context;
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

        paint2.setColor(Color.BLUE);
        paint2.setStyle(Paint.Style.FILL);

        paint3.setColor(Color.GREEN);
        paint3.setTextSize(80);
        paint3.setTypeface(Typeface.create(paint3.getTypeface(), Typeface.BOLD));
        paint3.setStyle(Paint.Style.FILL);


        paintServer.setColor(Color.GRAY);
        paintServer.setStyle(Paint.Style.FILL);


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
        //Log.e("hola", "hola");
        Log.e("WX, WY: ", ""+getWidth()+" "+getHeight());
        game.setWXY(getWidth(), getHeight() );
        game.addSpaceship(0);
        thread.setRunning(true);
        thread.start();
        //Log.e("Chau", "chau");
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

        //ServidorEnviarCoordenadas();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateState(event.getX(), event.getY(), wx, wy, 0);
                //if(game.getTurbo())
                //    doDraw(getHolder(),bitmapOn, (int)event.getX(), (int)event.getY(),1);
                //else
                //    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
                //Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                updateState(event.getX(), event.getY(), wx, wy,false, 0);
            //    doDraw(getHolder(),bitmapOff, (int)event.getX(), (int)event.getY(),1);
            //Log.d("TouchEvent", "getAction()" + "ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
                //updateState(event.getX(), event.getY(), wx, wy);
                //doDraw(getHolder(),bitmapOff  , (int)event.getX(), (int)event.getY(),1);
                break;
            case MotionEvent.ACTION_CANCEL:
                updateState(event.getX(), event.getY(), wx, wy,false, 0);
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
            updateToClients();
            //yy = yy - 5;
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ABAJO");
            game.rotateLeft(id);
            updateToClients();
            //yy = yy  + 5;
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click IZQUIERDA");
            game.rotateRight(id);
            updateToClients();
            //xx = xx  - 5;
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click DERECHA");
            game.rotateLeft(id);
            updateToClients();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click A");
            game.turbo(id);
            game.noRotate(id);
            updateToClients();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click B");
            //xx = xx  + 5;

            game.shot(id);
            updateToClients();
        }

    }

    public void updateState(float x,float y,int wx,int wy,boolean stop, int id){
        //System.out.println("TouchEvent111"+"X:" + event.getX() + ",Y:" + event.getY());
        if( 70 < x && x<130 && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click ARRIBA", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ARRIBA");
            game.noRotate(id);
            updateToClients();
            //yy = yy - 5;
        }

        if( 70 < x && x<130 && (wy-80)<y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click ABAJO", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click ABAJO");
            game.noRotate(id);
            updateToClients();
            //yy = yy  + 5;
        }

        if( 20 < x && x<80 && (wy-130)< y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click IZQUI", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click IZQUIERDA");
            game.noRotate(id);
            updateToClients();
            //xx = xx  - 5;
        }

        if( 120 < x && x<180 && (wy-130)<y && y<(wy-70) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click DERECHA");
            game.noRotate(id);
            updateToClients();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-180)< y && y<(wy-120) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click A");
            //game.turbo(0);
            game.noRotate(id);
            updateToClients();
            //xx = xx  + 5;
        }

        if( (wx-180) < x && x<(wx-120) && (wy-80)< y && y<(wy-20) ){
            //Toast.makeText(getApplicationContext(), "click DER", Toast.LENGTH_SHORT).show();
            //System.out.println(" Click B");
            //xx = xx  + 5;
            updateToClients();
        }

    }
    // Surfaceが作られた時呼び出される


    //public void update(){ game.update(getWidth(), getHeight());};
    public void update(){
        game.update(getWidth(), getHeight(),dpx,mdpx );
        playingVisible = game.getShips().get(0).getLiveVisible();
        playing = game.getShips().get(0).getLive();
    } /*noamlr*/


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

        if(playingVisible && playing) {
            canvas.drawCircle(100, wy - 150, 30, paint2); // up arrow

            canvas.drawCircle(100, wy - 50, 30, paint2); // down arrow

            canvas.drawCircle(50, wy - 100, 30, paint2); // left arrow

            canvas.drawCircle(150, wy - 100, 30, paint2); // right arrow

            canvas.drawCircle(wx - 150, wy - 150, 30, paint2); // a button

            canvas.drawCircle(wx - 150, wy - 50, 30, paint2); // b button

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

        //canvas.drawRect(xx, yy, xx+10, yy+10, paint3);
        //canvas.drawText("Envio FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF  x=" + xx + " y=" + yy, 50, 10,paintServer);

        //if (flat == 1){
        //    canvas.drawBitmap(bitmap, pos.getX(), pos.getY(), null);
        //}


        dpx = convertPixelsToDp(43.0f,context)/2.0f;
        dpy = convertPixelsToDp(40.0f,context)/2.0f;
        Log.e("ERR",""+dpx+" "+dpy);
        int cnt = -1;
        for(Spaceship ship : game.getShips()){
            cnt++;
            if(!ship.getLiveVisible()) continue;
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
        mdpy = convertPixelsToDp(6.0f, context) / 2.0f;

        for(Missil tmpmissil : game.getMissil()) {
            if (!tmpmissil.getActive()) continue;
            Matrix matrix = new Matrix();
            matrix.postRotate(tmpmissil.getAnglePosition(), mdpx, mdpy);
            matrix.postTranslate(tmpmissil.getPosX() - mdpx, tmpmissil.getPosY() - mdpy);
            canvas.drawBitmap(bitmapMissil, matrix, null);
            canvas.drawCircle(tmpmissil.getPosX(), tmpmissil.getPosY(), 6, paint3);
        }
    }


    public synchronized void setGame(Game tmp,int id){
        game=tmp;
        //game.addSpaceship(id);
        updateToClients(id);
    }

    public Game getGame(){
        return game;
    }


    public static float convertPixelsToDp(float px, Context context){
        return px * context.getResources().getDisplayMetrics().density;
    }

}

