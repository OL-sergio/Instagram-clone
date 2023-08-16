package udemy.java.instagram_clone.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;

public class GeneralUtils {

    private GeneralUtils() {
    }

    public static Bitmap generateCircularBitmap(Bitmap input) {

        final int width = input.getWidth();
        final int height = input.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);


        final Path path = new Path();



        path.addRect(
                (float) ( width ),
                (float) ( height ),
               0,
               0,
                Path.Direction.CCW
        );

        /*
        *
        RectF rectF = new RectF(0, 0, width, height);

        float[] corners = new float[] {
                20, 20,    // Top left radius in px
                20, 20,    // Top right radius in px
                20, 20,    // Bottom right radius in px
                20, 20     // Bottom left radius in px
        };

        path.addRoundRect(

                rectF,
                corners,
                Path.Direction.CCW
                );


         */

       /*
       * RectF rectF = new RectF(0, 0, width, height);

        path.addArc(
                rectF,
                0f,
                180f
        );*/


      /* path.addCircle(
                (float) (width / 2)
                , (float) (height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW
        );*/


        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(input, 0, 0, null);
        return outputBitmap;
    }
}
