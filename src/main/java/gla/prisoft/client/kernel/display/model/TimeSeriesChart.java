package gla.prisoft.client.kernel.display.model;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;


public class TimeSeriesChart extends JPanel {
	private static final long serialVersionUID = -4933097081367948040L;
	
	//timeseries variable
	private TimeSeries series1;
	private TimeSeries series2;
	private TimeSeries series3;
	private TimeSeries series4;
	private JFreeChart result;
	
	Second current = new Second( ); 
		
	@SuppressWarnings("deprecation")
	public TimeSeriesChart(){		
		this.setBackground(new Color(255,255,255));
				
//		if(SatSerializer.requirementHtmlFullDesc.length() >0){
//			JLabel reqLabel = new JLabel();
//	    	reqLabel.setText("<html>F={"+SatSerializer.requirementHtmlFullDesc+"}</html>");
//	    	this.add(reqLabel);	
//		}    	
    	
		this.series1 = new TimeSeries("pathSat(pr)", Second.class);
		this.series2 = new TimeSeries("feasibility(pr)", Second.class);
		this.series3 = new TimeSeries("cost(pr)", Second.class);
		this.series4 = new TimeSeries("benefit(pr)", Second.class);
		final TimeSeriesCollection dataset1 = new TimeSeriesCollection(this.series1);
		final TimeSeriesCollection dataset2 = new TimeSeriesCollection(this.series2); 
		final TimeSeriesCollection dataset3 = new TimeSeriesCollection(this.series3); 
		final TimeSeriesCollection dataset4 = new TimeSeriesCollection(this.series4); 
		
//		final JFreeChart chart1 = createChart(dataset1,dataset2,dataset3, "",0.0, 15); //pathsat(pr), cost, feasibility
		final JFreeChart chart1 = createChart(dataset1,dataset2,dataset3,dataset4, "",0.0, 4); //pathsat(pr), cost, feasibility
//		final JFreeChart chart2 = createChart(dataset2,"feasibility(pr)",0.0, 4);
		
		final ChartPanel chartPanel1 = new ChartPanel(chart1);
//		final ChartPanel chartPanel2 = new ChartPanel(chart2);

		final JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,  BoxLayout.Y_AXIS));
        content.add(chartPanel1);
//        content.add(chartPanel2);
        
        chartPanel1.setPreferredSize(new java.awt.Dimension(1090, 300));
        chartPanel1.setMouseZoomable(true, false);
        chartPanel1.setMouseWheelEnabled(true);
        chartPanel1.setHorizontalAxisTrace(true);
        chartPanel1.setVerticalAxisTrace(true);
        chartPanel1.setHorizontalAxisTrace(true);
        chartPanel1.setVerticalAxisTrace(true);
        
//        chartPanel2.setPreferredSize(new java.awt.Dimension(1090, 200));
//        chartPanel2.setMouseZoomable(true, false);
//        chartPanel2.setMouseWheelEnabled(true);
//        chartPanel2.setHorizontalAxisTrace(true);
//        chartPanel2.setVerticalAxisTrace(true);
//        chartPanel2.setHorizontalAxisTrace(true);
//        chartPanel2.setVerticalAxisTrace(true);
        
        this.add(content);
        
        this.setVisible(true);        
	}
		
	public void insertData1(double sat){

		this.series1.add(current, sat);
//		current = ( Second ) current.next( ); 
	}
	
	public void insertData2(double feasibility){
		if(feasibility >4){
			this.series2.add(current, 4);
		}
		else{
			this.series2.add(current, feasibility);			
		}
//		this.series2.add(current, feasibility);			
//		current = ( Second ) current.next( ); 
	}
	
	public void insertData3(double cost){
		this.series3.add(current, cost);
	}
	
	public void insertData4(double benefit){
		this.series4.add(current, benefit);
		current = ( Second ) current.next( ); 
	}
	
	public void addMarker(String label){
		XYPlot plot = result.getXYPlot();
		final Marker originalEnd = new ValueMarker(current.getFirstMillisecond());//m_orgStartTime is the calc time 
		originalEnd.setPaint(Color.orange);
		originalEnd.setLabel(label);
		originalEnd.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		originalEnd.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		plot.addDomainMarker(originalEnd);
	}
	
    private JFreeChart createChart(final XYDataset dataset1, final XYDataset dataset2, final XYDataset dataset3,final XYDataset dataset4, String ylabel, double rangeLower, double rangeUpper) {
        result = ChartFactory.createTimeSeriesChart(
            "", 
            "", //protocol 
            ylabel,//pathSat(pr)
            dataset1, // initial series
            true, 
            true, 
            false
        );
        result.setBackgroundPaint(Color.white);
        
        final XYPlot plot1 = result.getXYPlot();
        plot1.setBackgroundPaint(new Color(0xffffe0));
        plot1.setDomainGridlinesVisible(true);
        plot1.setDomainGridlinePaint(Color.lightGray);
        plot1.setRangeGridlinePaint(Color.lightGray);
        
        ValueAxis axis1 = plot1.getDomainAxis();
        axis1.setAutoRange(true);
        
     // render shapes and lines
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, false);
        renderer1.setBaseShapesVisible(false);
        renderer1.setBaseShapesFilled(false);
//        Stroke stroke1 = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
//        renderer1.setBaseOutlineStroke(stroke1);
        plot1.setRenderer(0,renderer1);
        
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setBaseShapesVisible(false);
        renderer2.setBaseShapesFilled(false);
        plot1.setRenderer(1,renderer2);
        
        XYLineAndShapeRenderer renderer3 = new XYLineAndShapeRenderer(true, false);
        renderer3.setBaseShapesVisible(false);
        renderer3.setBaseShapesFilled(false);
        plot1.setRenderer(2,renderer3);
        
      
        axis1 = plot1.getRangeAxis();
//        axis1.setRange(0.0, 1.2); 
        plot1.setDataset(0,dataset1);
        plot1.setDataset(1,dataset2);
        plot1.setDataset(2,dataset3);
        plot1.setDataset(3,dataset4);
        axis1.setRange(rangeLower, rangeUpper); 
        return result;
        
        
//        plot.setDataset(0, dataset0);
//        plot.setRenderer(0, renderer0)
//
//        plot.setDataset(1, dataset1);
//        plot.setRenderer(1, renderer1)
    }
   
}
