package cis350.upenn.edu.sapling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import java.util.Timer;
import java.util.TimerTask;


public class ShowcaseActivity extends AppCompatActivity implements OnShowcaseEventListener {
    ShowcaseView showcase;
    ShowcaseView graph_sv;
    ShowcaseView bar_sv;
    ShowcaseView menu_sv;
    View.OnClickListener onClick;

    private int nextImg = 1;
    private int nextShowcase = 1;

    RelativeLayout.LayoutParams lps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcase);

        onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchShowcase();
            }
        };

        createAllShowcaseViews();

//Optional delay between creation of activity and showcase view
//        CountDownTimer cdt = new CountDownTimer(2500, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//            @Override
//            public void onFinish() {
//                showcase.show();
//            }
//        };
//        cdt.start();

      //graph_sv.hide();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView plant_view = (ImageView) findViewById(R.id.homepage_plant);
                        switchImage(plant_view);
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }


    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

    private void createAllShowcaseViews() {
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        ViewTarget graph_target = new ViewTarget(R.id.graph_showcase, this);
        graph_sv = new ShowcaseView.Builder(this)
                .setShowcaseDrawer(new SquareShowcaseView(getResources(), 1))
                .setTarget(graph_target)
                .setContentTitle(R.string.sc_graph_title)
                .setContentText(R.string.sc_graph_descr)
                .setStyle(R.style.ShowcaseView)
                .setShowcaseEventListener(this)
                //.replaceEndButton(R.layout.view_custom_button)
                .build();
        graph_sv.setButtonPosition(lps);
        graph_sv.overrideButtonClick(onClick);
        graph_sv.hide();


        FrameLayout fr=(FrameLayout)findViewById(R.id.frameLayout);
        ViewTarget bar_target = new ViewTarget(R.id.bar, this);
        bar_sv = new ShowcaseView.Builder(this)
                .setShowcaseDrawer(new SquareShowcaseView(getResources(),2))
                .setTarget(bar_target)
                .setContentTitle(R.string.sc_bar_title)
                .setContentText(R.string.sc_bar_descr)
                .setStyle(R.style.ShowcaseView)
                .setShowcaseEventListener(this)
                .build();
        bar_sv.setButtonPosition(lps);
        bar_sv.overrideButtonClick(onClick);
        bar_sv.hide();

        ViewTarget menu_target = new ViewTarget(R.id.menu_target, this);
        menu_sv= new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(menu_target)
                .setContentTitle(R.string.sc_menu_title)
                .setContentText(R.string.sc_menu_descr)
                .setStyle(R.style.ShowcaseView)
                .setShowcaseEventListener(this)
                .build();
        menu_sv.setButtonPosition(lps);
        menu_sv.overrideButtonClick(onClick);
        menu_sv.hide();

        ViewTarget homeplant_target = new ViewTarget(R.id.homepage_plant, this);
        showcase = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(homeplant_target)
                .setContentTitle(R.string.sc_qof_title)
                .setContentText(R.string.sc_qof_descr)
                .setStyle(R.style.ShowcaseView)
                .setShowcaseEventListener(this)
                //.replaceEndButton(R.layout.view_custom_button)
                .build();
        showcase.setButtonPosition(lps);
        showcase.overrideButtonClick(onClick);
        showcase.show();
    }
private void switchShowcase () {
        switch (nextShowcase) {
            case 1:
                showcase.hide();
                showcase = graph_sv;
                showcase.show();
                nextShowcase = 2;
                break;
            case 2:
                showcase.hide();
                showcase = bar_sv;
                int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
                lps.setMargins(margin*2, margin,margin, margin * 20);
                showcase.setButtonPosition(lps);
                showcase.show();
                nextShowcase = 3;
                break;
            case 3:
                showcase.hide();
                showcase = menu_sv;
                 margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
                lps.setMargins(margin, margin,margin, margin);
                showcase.setButtonPosition(lps);
                showcase.show();
                nextShowcase = 4;
                break;
            case 4:
                showcase.hide();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
}
    private  void switchImage(ImageView plant_view) {
        switch (nextImg) {
            case 1:
                plant_view.setImageResource(R.drawable.ic_sapling_1);
                nextImg = 2;
                break;
            case 2:
                plant_view.setImageResource(R.drawable.ic_sapling_2);
                nextImg = 3;
                break;
            case 3:
                plant_view.setImageResource(R.drawable.ic_sapling_3);
                nextImg = 4;
                break;
            case 4:
                plant_view.setImageResource(R.drawable.ic_sapling_4);
                nextImg = 5;
                break;
            case 5:
                plant_view.setImageResource(R.drawable.ic_sapling_5);
                nextImg = 6;
                break;
            case 6:
                plant_view.setImageResource(R.drawable.ic_sapling_6);
                nextImg = 1;
                break;
            default:
                plant_view.setImageResource(R.drawable.ic_sapling_1);
                break;
        }

    }
}