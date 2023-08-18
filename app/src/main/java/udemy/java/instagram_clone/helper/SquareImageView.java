package udemy.java.instagram_clone.helper;



import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

//https://gist.github.com/lesleh/7469169

public class SquareImageView extends AppCompatImageView {

    public SquareImageView(Context context) {super(context);}

    public SquareImageView(Context context, AttributeSet attrs) { super(context, attrs); }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec, widthMeasureSpec );
    }

}
