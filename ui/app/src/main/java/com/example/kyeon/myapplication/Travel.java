package com.example.kyeon.myapplication;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Travel implements Serializable {

    static final long serialVersionUID = 1983928193;

    int travel_id;//travel도 id를 하나 두면 좋지않을까 생각 하는중
    String id;//user id
    String title;//여행 타이틀
    String cityName;//여행 장소 이름
    int people_count;//사람 수
    int days;//날짜수 1일차 ~ 4일차 같은거
    int smm, syy, sdd;//시작 날짜
    int emm, eyy, edd;//끝 날짜
    DailyDiary[] dailyDiary;//하루 일지
    transient Context context;// 서버에서는 저장 안해도 ㄱㅊ

    public Travel(Context context, String id, String cityName, String title, int people_count, int days, int smm, int syy, int sdd, int emm, int eyy, int edd)//여행 하나 초기화
    {
        this.context = context;
        this.id = id;
        this.title = title;
        this.cityName = cityName;
        this.days = days;
        this.people_count = people_count;
        this.dailyDiary = new DailyDiary[days];
        for (int i = 0; i < days; i++) {
            dailyDiary[i] = new DailyDiary();
        }

        this.smm = smm;
        this.syy = syy;
        this.sdd = sdd;
        this.emm = emm;
        this.eyy = eyy;
        this.edd = edd;
    }

    public void save() throws IOException
    {
        FileOutputStream fos = context.openFileOutput("travel_1", Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();
    }

    public static Travel load(Context context, String path) throws IOException, ClassNotFoundException
    {
        FileInputStream fis = context.openFileInput(path);
        ObjectInputStream is = new ObjectInputStream(fis);
        Travel travel = (Travel) is.readObject();
        is.close();
        fis.close();
        return travel;
    }


    public class DailyDiary implements Serializable//그날 하루 장소들에 대한 값들이 있을것
    {
        ArrayList<PlaceReview> review = new ArrayList<>();

        public DailyDiary ()
        {

        }

        public class PlaceReview implements Serializable//한장소에 대한 정보와 리뷰 데이터들이 있음
        {
            int place_id;//장소 id
            String place_name;//장소 이름
            int score;//장소 점수
            String reviewText;//장소 리뷰
            SerialBitmap image;//장소 이미지
            boolean reviewed;//장소 리뷰 작성 되었는지 여부

            public PlaceReview(int place_id, String place_name)
            {
                this.place_id = place_id;
                this.place_name = place_name;
                this.score = -1;
                this.reviewText = null;
                this.image = null;
                reviewed = false;
            }

            public void setReview(int place_id, String place_name, int score, String reviewText, SerialBitmap image)
            {
                this.place_id = place_id;
                this.place_name = place_name;
                this.score = score;
                this.reviewText = reviewText;
                this.image = image;
                reviewed = true;
            }
        }

        public void addPlace(int place_id, String place_name)//TripPlan단계에서 장소를 추가
        {
            review.add(new PlaceReview(place_id, place_name));
        }
        public void deletePlace(int place_index)//TripPlan단계에서 장소를 삭제
        {
            review.remove(place_index);
        }

        public void setPlaceReview(int place_index,int place_id, String place_name, int score, String reviewText, SerialBitmap image)//내 여행에서 리뷰를 작성하고 완료버튼 눌름
        {
            review.get(place_index).setReview(place_id, place_name, score, reviewText, image);
        }

        public PlaceReview getReview(int place_index)//내여행, 다른 여행에서 원하는 리뷰를 읽어옴
        {
            return review.get(place_index);
        }

    }

}
