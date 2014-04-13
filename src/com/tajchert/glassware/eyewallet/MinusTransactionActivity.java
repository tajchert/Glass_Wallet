package com.tajchert.glassware.eyewallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class MinusTransactionActivity extends Activity implements
		TextToSpeech.OnInitListener {

	private static final String LIVE_CARD_ID = "pjwstk_lesson_card";
	private List<Card> mCards = new ArrayList<Card>();
	private CardScrollView mCardScrollView;
	private ExampleCardScrollAdapter adapter;
	private TextToSpeech tts;
	private Card card1;
	private LiveCard mLiveCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tts = new TextToSpeech(this, this);
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
		createCards();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onStop();
	}

	private void createNoLessonCard() {
		mCards = new ArrayList<Card>();

		card1 = new Card(this);
		card1.setText("TEST 1");
		card1.setFootnote("");
		card1.setImageLayout(Card.ImageLayout.LEFT);
		card1.addImage(R.drawable.ic_launcher);
		mCards.add(card1);
		adapter.notifyDataSetChanged();
	}

	private void createCards() {
		mCards = new ArrayList<Card>();

		card1 = new Card(this);
		card1.setText("TEST MINUS!!");
		card1.setFootnote("");
		card1.setImageLayout(Card.ImageLayout.LEFT);
		card1.addImage(R.drawable.ic_launcher);
		mCards.add(card1);
		adapter.notifyDataSetChanged();
		// publishCard(LessonActivity.this);
	}

	private void speakOut(String in) {
		tts.speak(in, TextToSpeech.QUEUE_FLUSH, null);
	}

	private class ExampleCardScrollAdapter extends CardScrollAdapter {
		@Override
		public int findIdPosition(Object id) {
			return -1;
		}

		@Override
		public int findItemPosition(Object item) {
			return mCards.indexOf(item);
		}

		@Override
		public int getCount() {
			return mCards.size();
		}

		@Override
		public Object getItem(int position) {
			return mCards.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mCards.get(position).toView();
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				// btnSpeak.setEnabled(true);
				// speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}
	/*
	 * private void publishStaticCard(Context context, Card card) {
	 * TimelineManager tm = TimelineManager.from(context); tm.insert(card); }
	 * 
	 * private void publishCard(Context context) { try { unpublishCard(context);
	 * } catch (Exception e) { e.printStackTrace(); } if (mLiveCard == null) {
	 * TimelineManager tm = TimelineManager.from(context); mLiveCard =
	 * tm.createLiveCard(LIVE_CARD_ID);
	 * 
	 * // mLiveCard.setViews(new //
	 * RemoteViews(context.getPackageName(),R.layout.livecard_lesson)); Intent
	 * intent = new Intent(context, LessonActivity.class);
	 * mLiveCard.setAction(PendingIntent .getActivity(context, 0, intent, 0));
	 * RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
	 * R.layout.livecard_lesson); // Log.e("PJWSTK_GLASS", content);
	 * 
	 * remoteViews.setTextViewText(R.id.textViewContent,
	 * "PJWSTK\n\nNastêpne zajęcia"); //
	 * remoteViews.setCharSequence(R.id.textViewContent, "setText", // content);
	 * mLiveCard.setViews(remoteViews);
	 * mLiveCard.publish(LiveCard.PublishMode.SILENT); } else { // Card is
	 * already published. return; } }
	 * 
	 * private void unpublishCard(Context context) { if (mLiveCard != null) {
	 * mLiveCard.unpublish(); mLiveCard = null; } }
	 */

}
