package com.daemin.searchtime;

/**
 * Created by hernia on 2015-10-13.
 */
public class SearchFragment {
    /*@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setupRecommendDatas(String time) {
        if(User.USER.isSubjectDownloadState()) {
            //if(db.equals(null)) db = new DatabaseHandler(context);
            List<SubjectData> recommends = db.getRecommendSubjectDatas(time);
            if(recommends.size()==0){
                hlvRecommend.setVisibility(View.GONE);
                tvRecommendDummy.setVisibility(View.VISIBLE);
                tvRecommendDummy.setText(getResources().getString(R.string.nothing_schedule));
            }else{
                hlvRecommend.setVisibility(View.VISIBLE);
                tvRecommendDummy.setVisibility(View.GONE);
            }
            if(!adapterFlag) {
                adapter = new HorizontalListAdapter(this, recommends);
                hlvRecommend.setAdapter(adapter);
                hlvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> tempTimePos = new ArrayList<>();
                        Common.stateFilter(Common.getTempTimePos(), viewMode);
                        for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
                                .toString())) {
                            tempTimePos.add(timePos);
                            //TimePos.valueOf(timePos).setPosState(PosState.HALFANHOUR);
                        }
                        Common.setTempTimePos(tempTimePos);
                    }
                });
                adapterFlag=true;
            }else{
                adapter.notifyDataSetChanged();
            }
        }else{
            Toast.makeText(MainActivity.this, "먼저 대학을 선택하세요", Toast.LENGTH_SHORT).show();
        }
    }*/
}
