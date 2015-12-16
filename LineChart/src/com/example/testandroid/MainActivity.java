package com.example.testandroid;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.robot.widget.LineChart;
import com.robot.widget.LineChart.OnChartClickListener;
import com.robot.widget.LineData;
import com.robot.widget.PointInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LineChart lineChart = new LineChart(this);
		setContentView(lineChart);
		
		LineData data1 = new LineData();
		data1.xLabel = "一月";
		data1.yValue = 20;
		
		LineData data2 = new LineData();
		data2.xLabel = "二月";
		data2.yValue = 40;
		
		LineData data3 = new LineData();
		data3.xLabel = "三月";
		data3.yValue = 80;
		
		LineData data4 = new LineData();
		data4.xLabel = "四月";
		data4.yValue = -50;
		
		LineData data5 = new LineData();
		data5.xLabel = "五月";
		data5.yValue = -50;
		
		/////////////////////////////////////
		lineChart.setXAxisWidth(5);
		lineChart.setXAxisColor(0xFF00FFFF);
		lineChart.setXLabelColor(0xFF00FFFF);
		lineChart.setXLabelTextSize(20);
		
		
		lineChart.setYAxisWidth(5);
		lineChart.setYAxisColor(0xFF00FFFF);
		lineChart.setYLabelColor(0xFF00FFFF);
		lineChart.setYLabelTextSize(20);
		
		lineChart.setBaseValue(63);
		lineChart.setBaseLineColor(0xFFFF00FF);
		lineChart.setShowPointNums(3);
		
		lineChart.setMeshColor(Color.GREEN);
		//////////////////////////////////////
		
		
		ArrayList<LineData> lineData = new ArrayList<LineData>();
		lineData.add(data1);
		lineData.add(data2);
		lineData.add(data3);
		lineData.add(data4);
		lineData.add(data5);
		
		LineData data_1 = new LineData();
		data_1.yValue = 80;
		
		LineData data_2 = new LineData();
		data_2.yValue = 46;
		
		LineData data_3 = new LineData();
		data_3.yValue = -29;
		
		LineData data_4 = new LineData();
		data_4.yValue = -40;
		
		LineData data_5 = new LineData();
		data_5.yValue = 58;
		
		ArrayList<LineData> lineData1 = new ArrayList<LineData>();
		lineData1.add(data_1);
		lineData1.add(data_2);
		lineData1.add(data_3);
		
		
		
		
		LineData data_1_1 = new LineData();
		data_1_1.yValue = 56;
		
		LineData data_2_2 = new LineData();
		data_2_2.yValue = 78;
		
		LineData data_3_3 = new LineData();
		data_3_3.yValue = -54;
		
		LineData data_4_4 = new LineData();
		data_4_4.yValue = -30;
		
		LineData data_5_5 = new LineData();
		data_5_5.yValue = 43;
		
		ArrayList<LineData> lineData2 = new ArrayList<LineData>();
		lineData2.add(data_1_1);
		lineData2.add(data_2_2);
		lineData2.add(data_3_3);
		lineData2.add(data_4_4);
		lineData2.add(data_5_5);
		
		ArrayList<ArrayList<LineData>> datas = new ArrayList<ArrayList<LineData>>();
		datas.add(lineData);
		datas.add(lineData1);
		datas.add(lineData2);
		
		lineChart.setData(datas);
		
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("一月");
		labels.add("二月");
		labels.add("三月");
		labels.add("四月");
		labels.add("五月");
		labels.add("六月");
		
		lineChart.setXLabels(labels);
		
		
		lineChart.setOnChartClickListener(new OnChartClickListener() {
			
			@Override
			public void onChartClick(PointInfo pointInfo) {
				
				Log.i("lyh","lineId: " + pointInfo.lineId);
				Log.i("lyh","pointIndex: " + pointInfo.pointIndex);
				Log.i("lyh","value : " + pointInfo.value);
				
			}
		});
		
		
		
	}
	
	public Bitmap imageDownLoad(String path) {
		
		try {
			
			URL url = new URL(path);
			URLConnection con = (URLConnection)url.openConnection();
			InputStream input = con.getInputStream();
			
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			
			return bitmap;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
