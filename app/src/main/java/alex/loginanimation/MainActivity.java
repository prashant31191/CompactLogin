package alex.loginanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
	@BindView(R.id.b_register)
	FloatingActionButton registerButton;
	@BindView(R.id.b_login)
	Button loginButton;
	@BindView(R.id.b_close)
	ImageButton closeButton;
	@BindView(R.id.et_username)
	EditText usernameEditText;
	@BindView(R.id.et_password)
	EditText passwordEditText;
	@BindView(R.id.cv_back)
	CardView backCardView;
	@BindView(R.id.cv_login)
	CardView loginCardView;
	@BindView(R.id.tv_title)
	TextView titleTextView;

	@BindView(R.id.cv_register)
	CardView registerCardView;
	@BindView(R.id.l_register)
	RelativeLayout registerLayout;

	@BindDimen(R.dimen.z_force_above)
	@Dimension int zForceAbove;
	@BindDimen(R.dimen.card_elevation)
	@Dimension int loginCardElevation;

	final long duration = 800;
	final float alphaBack = 0.9f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.b_close)
	void onCloseRegistrationClick() {
		moveLoginViewToFront();

		/*PointF pStart = getXY(closeButton),
				pTarget = getXY(registerButton);
		pTarget.offset(-registerCardView.getX(), -registerCardView.getY());
		pTarget.offset(registerButton.getWidth() / 2 - closeButton.getWidth() / 2,
				registerButton.getHeight() / 2 - closeButton.getHeight() / 2);

		ValueAnimator circleAnimator = new CircleAnimator(pStart, pTarget, 0)//-120)
				.onViewPosition(closeButton)
				.counterClockwise()
				.setRadiusInterpolator(new AccelerateInterpolator(1 / 2.2f))
				.setDuration(duration);
		circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				*//*PointF xy = getXY(closeButton);
				xy.offset(
						pLayoutIdle.x - registerLayout.getX(),
						pLayoutIdle.y - registerLayout.getY());
				setXY(closeButton, xy);*//*
			}
		});
		circleAnimator.start();*/


		final ViewGroup animatingView = registerCardView;

		int endRadius = (int) (registerButton.getWidth() / 2f);
		int startRadius = (int) Math.ceil(Math.hypot(animatingView.getWidth(), animatingView.getHeight()));
		final PointF pRevealCenter = new PointF(animatingView.getWidth() / 2f, animatingView.getHeight() / 2f);

		Animator unrevealAnimator = ViewAnimationUtils
				.createCircularReveal(animatingView, ((int) pRevealCenter.x), ((int) pRevealCenter.y), startRadius, endRadius);
		unrevealAnimator
				.setDuration(duration)
				.setInterpolator(new DecelerateInterpolator(1.9f));
		unrevealAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animatingView.setVisibility(View.INVISIBLE);
				registerButton.setVisibility(View.VISIBLE);
			}
		});



		ObjectAnimator fadeContentAnimator = ObjectAnimator.ofFloat(registerLayout, View.ALPHA, 1f, 0f);
		fadeContentAnimator.setDuration((long) (duration * 0.4f));
		fadeContentAnimator.setInterpolator(new AccelerateInterpolator(0.2f));


		PointF pEnd = getXY(registerButton), pStart = getXY(registerCardView);
		pEnd.offset(-pRevealCenter.x + endRadius, -pRevealCenter.y + endRadius);

		CircleAnimator moveToFabAnimator = new CircleAnimator(pStart, pEnd, 45)
				.onViewPosition(registerCardView)
				.counterClockwise();
		moveToFabAnimator.setDuration((long) (duration * 0.6f));

		AnimatorSet fadeMoveAS = new AnimatorSet();
		fadeMoveAS.playSequentially(fadeContentAnimator, moveToFabAnimator);



		AnimatorSet unrevealAnimatorSet = new AnimatorSet();
		unrevealAnimatorSet.playTogether(unrevealAnimator, fadeMoveAS);

		unrevealAnimatorSet.start();
	}

	@OnClick(R.id.b_register)
	void onRegisterClick() {
		registerLayout.setAlpha(1f);
		moveLoginViewToBack();
		showRegisterView();
	}

	private void showRegisterView() {
		setXY(registerCardView, getXY(loginCardView));

		final ViewGroup animatingView = registerCardView;

		int startRadius = (int) (registerButton.getWidth() / 2f);
		int endRadius = (int) Math.ceil(Math.hypot(animatingView.getWidth(), animatingView.getHeight()));
		final PointF pRevealCenter = new PointF(animatingView.getWidth() * 0.8f, animatingView.getHeight() * 0.4f);

		animatingView.setVisibility(View.VISIBLE);
		animatingView.setZ(zForceAbove);

		float x = animatingView.getX() + pRevealCenter.x - startRadius,
				y = animatingView.getY() + pRevealCenter.y - startRadius;
		float bx = registerButton.getX(),
				by = registerButton.getY();

		final float dx = bx - x, dy = by - y;

		registerButton.setVisibility(View.INVISIBLE);

		final PointF pTarget = getXY(animatingView),
				pFab = getXY(animatingView);
		pFab.offset(dx, dy);
		setXY(animatingView, pFab);


		ValueAnimator xyAnimator = new CircleAnimator(pFab, pTarget, 90)
				.onViewPosition(animatingView)
				.setDuration((long) (duration * 0.5f));
		
		xyAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF point = (PointF) animation.getAnimatedValue();
				float xToTarget = pTarget.x - point.x,
						yToTarget = pTarget.y - point.y;
				registerLayout.setX(xToTarget);
				registerLayout.setY(yToTarget);
			}
		});

		final PointF pLayoutIdle = getXY(registerLayout);

		xyAnimator.setInterpolator(new AccelerateInterpolator());
		xyAnimator.start();






		Animator animator = ViewAnimationUtils
				.createCircularReveal(animatingView, ((int) pRevealCenter.x), ((int) pRevealCenter.y), startRadius, endRadius);
		animator
				.setDuration(duration)
				.setInterpolator(new AccelerateInterpolator(1.9f));
		animator.start();


		moveCloseButton(pRevealCenter, pLayoutIdle);
	}

	void moveCloseButton(final PointF pRevealCenter, final PointF pLayoutIdle) {
		final PointF
				pTarget = getXY(closeButton),
				pStart = centerToLeftTop(closeButton, pRevealCenter);

		Animator rotateIconAnimator = ObjectAnimator.ofFloat(closeButton, View.ROTATION, 0, 45);
		rotateIconAnimator
				.setDuration((long) (duration * 0.9f))
				.setInterpolator(new AccelerateDecelerateInterpolator());

		ValueAnimator circleAnimator = new CircleAnimator(pStart, pTarget, -120)
				.onViewPosition(closeButton)
				.setRadiusInterpolator(new AccelerateInterpolator(2.2f))
				.setDuration(duration);
		circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF xy = getXY(closeButton);
				xy.offset(
						pLayoutIdle.x - registerLayout.getX(),
						pLayoutIdle.y - registerLayout.getY());
				setXY(closeButton, xy);
			}
		});

		AnimatorSet resultingAnimator = new AnimatorSet();
		resultingAnimator.playTogether(circleAnimator, rotateIconAnimator);

		resultingAnimator.start();
	}

	void setXY(View view, PointF p) {
		view.setX(p.x);
		view.setY(p.y);
	}

	PointF getXY(View view) {
		return new PointF(view.getX(), view.getY());
	}

	PointF centerToLeftTop(View view, PointF p) {
		PointF pLeftTop = new PointF(p.x, p.y);
		pLeftTop.offset(-view.getWidth() / 2, -view.getHeight() / 2);
		setXY(view, pLeftTop);
		return pLeftTop;
	}

	void moveLoginViewToBack() {
		int widthDifference = loginCardView.getWidth() - backCardView.getWidth();

		loginCardView.animate()
				.setDuration(duration / 3)
				.setInterpolator(new DecelerateInterpolator())
				.alpha(alphaBack)
				.z(0)
				.y(backCardView.getTop())
				.scaleX(getNeededScaleX(loginCardView, widthDifference));

		backCardView.animate()
				.setDuration(duration / 3)
				.setInterpolator(new DecelerateInterpolator())
				.yBy(backCardView.getTop() - loginCardView.getTop())
				.scaleX(getNeededScaleX(backCardView, widthDifference));
	}

	void moveLoginViewToFront() {
		int widthDifference = registerCardView.getWidth() - loginCardView.getWidth();

		backCardView.animate()
				.setDuration(duration / 3)
				.setInterpolator(new AccelerateInterpolator())
				.y(loginCardView.getY())
				.scaleX(getNeededScaleX(backCardView, widthDifference));

		loginCardView.animate()
				.setDuration(duration / 3)
				.setInterpolator(new AccelerateInterpolator())
				.alpha(1f)
				.z(loginCardElevation)
				.y(registerCardView.getY())
				.scaleX(getNeededScaleX(loginCardView, widthDifference));
	}

	float getNeededScaleX(View view, int deduct) {
		return (float) (view.getWidth() - deduct) / view.getWidth();
	}

	/*private Animator getTranslationAnimator(View view, Interpolator interpolator,
	                                        Property<View, Float> property, float from, float to) {
		Animator animator = ObjectAnimator.ofFloat(view, property, from, to);
		animator.setInterpolator(interpolator);
		return animator;
	}

	private Animator getCurveXYAnimator(View view, final PointF pFrom, final PointF pTo, boolean isCurveX) {
		AnimatorSet animatorSet = new AnimatorSet();

		Interpolator
				xInterpolator = new AccelerateInterpolator(1.5f),
				yInterpolator = new DecelerateInterpolator(0.7f);
		if (isCurveX) {
			Interpolator temp;
			temp = xInterpolator;
			//noinspection SuspiciousNameCombination
			xInterpolator = yInterpolator;
			yInterpolator = temp;
		}

		Animator xAnimator = getTranslationAnimator(view, xInterpolator, View.X, pFrom.x, pTo.x);
		Animator yAnimator = getTranslationAnimator(view, yInterpolator, View.Y, pFrom.y, pTo.y);
		animatorSet.playTogether(xAnimator, yAnimator);
		return animatorSet;
	}*/
}
