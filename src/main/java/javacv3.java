import org.bytedeco.javacv.*;

public class javacv3 {
    public static void main(String[] args)
    {

        String inputFile = "rtsp://admin:@192.168.123.6:554/ch0_1.264";
        // Decodes-encodes
        String outputFile = "recorde.mp4";
        try {
            frameRecord(inputFile, outputFile,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 按帧录制视频
     *
     * @param inputFile-该地址可以是网络直播/录播地址，也可以是远程/本地文件路径
     * @param outputFile
     *            -该地址只能是文件地址，如果使用该方法推送流媒体服务器会报错，原因是没有设置编码格式
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     * @throws org.bytedeco.javacv.FrameRecorder.Exception
     */
    public static void frameRecord(String inputFile, String outputFile, int audioChannel)
    {

        boolean isStart=true;//该变量建议设置为全局控制变量，用于控制录制结束
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, audioChannel);
        // 开始取视频源
        recordByFrame(grabber, recorder, isStart);

    }

    private static void recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder, Boolean status)
    {
        try {//建议在线程中使用该方法
            try {
                grabber.start();
                grabber.setOption("rtsp_transpor", "tcp");


            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
            try {
                recorder.start();
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
            Frame frame = null;
            while (true) {
                try {
                    if (!(status&& (frame = grabber.grabFrame()) != null)) break;


                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
                try {
                    recorder.record(frame);
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                recorder.stop();
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
            try {
                grabber.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (grabber != null) {
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
