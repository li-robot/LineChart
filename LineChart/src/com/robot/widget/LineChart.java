package com.robot.widget;

import java.util.ArrayList;

import com.uzmap.pkg.uzmodules.uzbrokenLine.utils.XLabels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LineChart extends View {

	public static final String TAG = "lyh";

	private int chartWidth;
	private int chartHeight;
	private int borderWidth = DensityUtil.dip2px(this.getContext(), 28);

	private int maxValue = 100;
	private int minValue = -40;

	private int step = 20;

	private int showPointNum = 3;

	private int moveOffset = 0;
	
	private Bitmap bgBitmap;
	
	private int bgColor = Color.TRANSPARENT;
	
	private int[] colors = {0xffcb3d1b, 0xfff6a800, 0xfffff100, 0xff00a13f, 0xff007db0, 0xff142a8c ,0xffa51466};

	public LineChart(Context context) {
		super(context);
		initPaint();
	}
	
	public void setShowPointNums(int nums){
		if(nums <= 1){
			this.showPointNum = 1;
		} else {
			this.showPointNum = nums - 1;
		}
	}
	
	public void setMaxValue(int maxValue){
		this.maxValue = maxValue;
	}
	
	public void setMinValue(int minValue){
		this.minValue = minValue;
	}
	
	public void setInterval(int interval){
		this.step = interval;
	}

	public LineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public LineChart(Context context, AttributeSet attrs, int id) {
		super(context, attrs, id);
		initPaint();
	}

	private boolean isFirst = true;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		this.chartWidth = getWidth();
		this.chartHeight = getHeight();

		if (isFirst) {
			borderWidth = getYLabelWidth();
			init();
			isFirst = false;
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		// draw chart background
		drawChartBg(canvas);
		
		
		// draw X Axis
		drawXAxis(canvas);
		drawXAxisLabel(canvas);

		// draw Y Axis
		drawYAxis(canvas);
		// draw data line
		drawLine(canvas);
		
		
		drawYAxisLabel(canvas);
		
		// draw baseLine
		drawBaseLine(canvas);

	}
	
	private ArrayList<Point> xPoints = new ArrayList<Point>();

	public void init() {

		int unit = (chartWidth - borderWidth) / showPointNum;

		for (int j = 0; j < datas.size(); j++) {
			ArrayList<LineData> lineDatas = datas.get(j);

			for (int i = 0; i < lineDatas.size(); i++) {

				LineData lineData = lineDatas.get(i);

				Point point = new Point();
				point.x = i * unit + borderWidth;
				point.y = chartHeight - chartHeight
						* (lineData.yValue - minValue) / (maxValue - minValue);

				lineData.point = point;

			}
		}
		
		for(int i=0; i< xLabels.size(); i++){
			
			Point point = new Point();
			point.x = i * unit + borderWidth;
			xPoints.add(point);

		}

	}
	
	private Rect src = new Rect();
	private Rect target = new Rect();
	private Paint bgPaint = new Paint();
	
	public void drawChartBg(Canvas canvas){
		
		if(bgBitmap != null){
			
			src.left = 0;
			src.top = 0;
			src.right = bgBitmap.getWidth();
			src.bottom = bgBitmap.getHeight();
			
			target.left = 0;
			target.top = 0;
			target.right = chartWidth;
			target.bottom = chartHeight;
			
			canvas.drawBitmap(bgBitmap, src, target, yAxisPaint);
			
		} else {
			
			target.left = 0;
			target.top = 0;
			target.right = chartWidth;
			target.bottom = chartHeight;
			bgPaint.setColor(bgColor);
			canvas.drawRect(target, bgPaint);
		}
	}
	
	public void setLineColors(int[] colors){
		this.colors = colors;
	}
	
	public void setBackgroundBitmap(Bitmap bitmap){
		this.bgBitmap = bitmap;
	}
	
	public void setBackgroundColor(int color){
		this.bgColor = color;
	}

	/**
	 * Y轴的画笔
	 */
	private Paint yAxisPaint;
	
	public void setYAxisWidth(int width){
		yAxisPaint.setStrokeWidth(width);
	}
	
	public void setYAxisColor(int color){
		yAxisPaint.setColor(color);
	}
	
	
	/**
	 * Y轴标签的画笔
	 */
	private Paint yAxisLabelPaint;
	
	public void setYLabelColor(int color){
		yAxisLabelPaint.setColor(color);
	}
	
	public void setYLabelTextSize(int textSize){
		yAxisLabelPaint.setTextSize(textSize);
	}

	/**
	 * X轴的画笔
	 */
	private Paint xAxisPaint;
	
	public void setXAxisWidth(int width){
		xAxisPaint.setStrokeWidth(width);
	}
	
	public void setXAxisColor(int color){
		xAxisPaint.setColor(color);
	}

	/**
	 * X轴标签的画笔
	 */
	private Paint xAxisLabelPaint;
	
	public void setXLabelColor(int textColor){
		xAxisLabelPaint.setColor(textColor);
	}
	
	public void setXLabelTextSize(int textSize){
		xAxisLabelPaint.setTextSize(textSize);
	}
	
	/**
	 * 网格画笔
	 */
	private Paint meshPaint;
	
	public void setMeshColor(int color){
		meshPaint.setColor(color);
	}

	public void initPaint() {

		yAxisPaint = new Paint();
		yAxisLabelPaint = new Paint();

		xAxisPaint = new Paint();
		xAxisLabelPaint = new Paint();
		
		meshPaint = new Paint();

	}

	private Path linePath = new Path();
	private Paint linePaint = new Paint();
	
	public void drawLine(Canvas canvas) {
		
		for (int j = 0; j < datas.size(); j++) {

			ArrayList<LineData> lineDatas = datas.get(j);
			
			// reset path
			linePath.reset();
			
			// reset linePaint
			if(j < colors.length){
				linePaint.setColor(colors[j]);
			}
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(3);
			linePaint.setStyle(Style.STROKE);
			
			for (int i = 0; i < lineDatas.size(); i++) {

				LineData cur = lineDatas.get(i);
				if (i == 0) {
					linePath.moveTo(cur.point.x + moveOffset, cur.point.y);
				} else {
					linePath.lineTo(cur.point.x + moveOffset, cur.point.y);
				}

			}
			canvas.drawPath(linePath, linePaint);
		}

		// draw node
		for (int j = 0; j < datas.size(); j++) {

			ArrayList<LineData> lineDatas = datas.get(j);
			if(j < colors.length){
				linePaint.setColor(colors[j]);
			}
			for (int i = 0; i < lineDatas.size(); i++) {
				LineData lineData = lineDatas.get(i);
				linePaint.setStrokeWidth(12);
				canvas.drawPoint(lineData.point.x + moveOffset,
						lineData.point.y, linePaint);
			}
		}
	}

	/**
	 * line data
	 */
	private ArrayList<ArrayList<LineData>> datas = new ArrayList<ArrayList<LineData>>();
	
	public ArrayList<ArrayList<LineData>> getDatas(){
		return datas;
	}
	
	/**
	 * x 轴的标签数组
	 */
	private ArrayList<String> xLabels = new ArrayList<String>();
	
	public void setXLabels(ArrayList<String> xLabels){
		this.xLabels = xLabels;
	}
	
	public void setData(ArrayList<ArrayList<LineData>> datas) {
		this.datas = datas;
	}

	private Rect coverRect = new Rect();
	private Paint coverRecrPaint = new Paint();
	
	public void drawYAxis(Canvas canvas) {
		canvas.drawLine(borderWidth, 0, borderWidth, chartHeight, yAxisPaint);
	}
	
	private Rect yLabelRect = new Rect();

	public void drawYAxisLabel(Canvas canvas) {
		
		coverRect.left = 0;
		coverRect.top = 0;
		coverRect.right = borderWidth - DensityUtil.dip2px(getContext(), 2);
		coverRect.bottom = chartHeight;
		
		coverRecrPaint.setColor(bgPaint.getColor());
		coverRecrPaint.setStyle(Style.FILL);
		canvas.drawRect(coverRect, coverRecrPaint);

		yAxisLabelPaint.setAntiAlias(true);
		int unit = (maxValue - minValue) / step;
		
		if(unit <= 0){
			return;
		}
		int avgHeight = chartHeight / unit;

		int minValuePointY = chartHeight;

		for (int i = minValue; i <= maxValue; i += step) {
			
			yLabelRect.left = 0;
			yLabelRect.right = borderWidth;
			
			if(i == minValue){
				yLabelRect.top = minValuePointY - DensityUtil.dip2px(getContext(), 12);
				yLabelRect.bottom = minValuePointY - DensityUtil.dip2px(getContext(), 6);
			} else {
				yLabelRect.top = minValuePointY - DensityUtil.dip2px(getContext(), 10);
				yLabelRect.bottom = minValuePointY + DensityUtil.dip2px(getContext(), 10);
			}
			
			if(i == maxValue) {
				yLabelRect.top = minValuePointY + DensityUtil.dip2px(getContext(), 6);
				yLabelRect.bottom = minValuePointY + DensityUtil.dip2px(getContext(), 12);
			}
			
			// draw mesh
			canvas.drawLine(borderWidth, minValuePointY, chartWidth, minValuePointY, meshPaint);
			
			drawTextInCenter(canvas, yAxisLabelPaint, yLabelRect,String.valueOf(i));
			
			minValuePointY -= avgHeight;
			
		}

	}

	public void drawXAxis(Canvas canvas) {

		int zeroY = chartHeight - chartHeight * -minValue
				/ (maxValue - minValue);
		canvas.drawLine(borderWidth, zeroY, chartWidth, zeroY, xAxisPaint);

	}
	
	private Rect xLabelRect = new Rect();

	public void drawXAxisLabel(Canvas canvas) {

		int zeroY = chartHeight - chartHeight * -minValue
				/ (maxValue - minValue);
		
		int xLabelsSize = xLabels.size();
		
		int unit = (chartWidth - borderWidth) / showPointNum;
		
		for (int i = 0; i < xLabelsSize ; i++) {

			int leftTopX = 0;
			if(i == 0){
				leftTopX = borderWidth +  moveOffset;
			} else {
				leftTopX = i * unit + borderWidth - DensityUtil.dip2px(getContext(), 15) + moveOffset;
			}
			
			int leftTopY = zeroY + DensityUtil.dip2px(getContext(), 10);
			int rightBottomX;
			if(i == xLabelsSize - 1){
				rightBottomX = i * unit + borderWidth - getXLabelWidth(xLabels.get(i)) + moveOffset;
			} else {
				rightBottomX = i * unit + borderWidth + moveOffset;
			}
			
			int rightBottomY = zeroY + DensityUtil.dip2px(getContext(), 20);

			xLabelRect.left = leftTopX;
			xLabelRect.top = leftTopY;
			xLabelRect.right = rightBottomX;
			xLabelRect.bottom = rightBottomY;
			
			if(i == 0){
				drawTextInCenter(canvas, xAxisLabelPaint, xLabelRect, xLabels.get(i), borderWidth + DensityUtil.dip2px(getContext(),15) + moveOffset);
			} else {
				drawTextInCenter(canvas, xAxisLabelPaint, xLabelRect, xLabels.get(i));
			}
			
			// draw mesh
			if(i == 0)
				continue;
			canvas.drawLine(i * unit + borderWidth + moveOffset, 0, i * unit + borderWidth + moveOffset, chartHeight, meshPaint);
			
		}
	}
	
	private Paint baseLinePaint = new Paint();
	private int baseValue;
	
	public void setBaseLineColor(int baseLineColor){
		baseLinePaint.setColor(baseLineColor);
	}
	
	public void setBaseValue(int baseValue){
		this.baseValue = baseValue;
	}
	
	public void drawBaseLine(Canvas canvas){
		
		int baseY = chartHeight - chartHeight * (baseValue - minValue) / (maxValue - minValue);
		baseLinePaint.setStrokeWidth(5);
		canvas.drawLine(borderWidth, baseY, chartWidth, baseY, baseLinePaint);
		
	}

	public void drawTextInCenter(Canvas canvas, Paint paint, Rect targetRect,
			String xVal) {

		FontMetricsInt fontMetrics = paint.getFontMetricsInt();

		// Referenced from http://blog.csdn.net/hursing
		int baseline = targetRect.top
				+ (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
				/ 2 - fontMetrics.top;
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(xVal, targetRect.centerX(), baseline, paint);

	}
	
	public void drawTextInCenter(Canvas canvas, Paint paint, Rect targetRect,
			String xVal, int x) {

		FontMetricsInt fontMetrics = paint.getFontMetricsInt();

		// Referenced from http://blog.csdn.net/hursing
		int baseline = targetRect.top
				+ (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
				/ 2 - fontMetrics.top;
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(xVal, x, baseline, paint);

	}

	public Point getLastPoint() {
		if(xPoints.size() > 0){
			return xPoints.get(xPoints.size() - 1);
		}
		return null;
	}

	private int downX = 0;
	private int downY = 0;
	private int moveDistance = 0;
	private int hasMoveDistance = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			moveDistance = (int) (event.getX() - downX);
			moveOffset = hasMoveDistance + moveDistance;
			
			if(getLastPoint().x <= chartWidth){
				return true;
			}

			// left most
			if (moveOffset >= 0) {
				moveOffset = 0;
			}

			// right most
			if (moveOffset < chartWidth - getLastPoint().x) {
				moveOffset = chartWidth - getLastPoint().x;
			}

			this.invalidate();
			break;
		case MotionEvent.ACTION_UP:
			hasMoveDistance = moveOffset;
			
			// XXX : 判断是否在点击区域内
			if(Math.abs(event.getX() - downX) < 200 
					&& Math.abs(event.getY() - downY) < 200 ){
				
				PointInfo info = checkPoint(new Point((int)event.getX(), (int)event.getY()));
				if(info != null && mOnChartClickListener != null){
					mOnChartClickListener.onChartClick(info);
				}
			}
			break;
		}
		return true;
	}
	
	public interface OnChartClickListener {
		public void onChartClick(PointInfo pointInfo);
	}
	
	public OnChartClickListener mOnChartClickListener;
	
	public void setOnChartClickListener(OnChartClickListener mOnChartClickListener){
		this.mOnChartClickListener = mOnChartClickListener;
	}
	
	public PointInfo checkPoint(Point point){
		
		for(int i=0; i<datas.size(); i++){
			ArrayList<LineData> lineDatas = datas.get(i);
			
			for(int j=0; j<lineDatas.size(); j++){
				LineData data = lineDatas.get(j);
				if(Math.abs(data.point.x + moveOffset - point.x) < 30 
						&& Math.abs(data.point.y - point.y) < 30){
					
					PointInfo info = new PointInfo();
					info.lineId = i;
					info.pointIndex = j;
					info.value = data.yValue;
					
					return info;
				}
			}
		}
		
		return null;
	}
	
	public int getYLabelWidth(){
		
		int maxLabelWidth = (int)yAxisLabelPaint.measureText(maxValue + "");
		int minLableWidth = (int)yAxisLabelPaint.measureText(minValue + "");
		 
		return maxLabelWidth > minLableWidth ? maxLabelWidth : minLableWidth;
	}
	
	public int getXLabelWidth(String text){
		return (int)xAxisLabelPaint.measureText(text);
	}

	
}
