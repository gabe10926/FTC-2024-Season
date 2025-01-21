package org.firstinspires.ftc.teamcode;

import java.awt.font.NumericShaper;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;

import android.util.Size;

import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.VisionPortal.Size;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Autonomous(name="MecAuto", group="Robot")

public class MecAuto extends LinearOpMode {
    //I'll adjust thsi as we test, 
    //Its the distance of your robtot from the target/april tag
    final double DESIRED_DISTANCE = 12.0;

    //This is the rate at which the bot increases its speed
    final double SPEED_GAIN = 0.03; 

    //This is the rate at which the strafing speed changes
    final double STRAFE_GAIN = 0.015;

    //This is the rate at which the robot turns
    final double TURN_GAIN = 0.01;

    //These are our max speeds

    //this is the max speed of our robot(motors)
    final double MAX_AUTO_SPEED = 0.5;

    //this is the max speed of our strafe
    final double MAX_AUTO_STRAFE = 0.5;

    //this is the max speed of our turning 
    final double MAX_AUTO_TURN = 0.3;


    //declaring motors + servos
    public DcMotor  leftDrive   = null; //the left drivetrain motor
    public DcMotor  rightDrive  = null; //the right drivetrain motor
    public DcMotor  armMotor    = null; //the arm motor
    public CRServo  intake      = null; //the active intake servo
    public CRServo  wrist     = null; //the wrist servo


    private static final boolean USE_WEBCAM = true;
    private static final int DESIRED_TAG_ID = 584;
    private static VisionProtal visionPortal;
    private AprilTagProcessor aprilTag;
    private AprilTagDetection desiredTag = null;


    @Override public void runOpMode() {
        boolean targetFound = false; //set to true  when an AprilTag target is dedicated
        double drive = 0; //This is your desired foward power/speed (-1,1)
        double strafe = 0; // This is your desired  starfe power/speed (-1, 1)
        boolean turn  = 0; //This is your desired turning Speed (-1,1)


        //Initialize the April Tag detection process
        intAprilTag();

        //Initialize the hardware viarables. 
        leftFrontDrive = hardwareMap.get(DcMotor.class, "drive0");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "drive1");
        leftBackDrive = hardwareMap.get(DcMotor.class, "drive2");
        rightBackDrive = hardwareMap.get(DcMotor.class, "drive3");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FOrWARD);

     if (USE_WEBCAM)
            setManualExposure(exposureMS:6, gain:250); //Use low exposure time to reduce motion blur


            //wait for driver to press start
            telemetry.addData("Camera Preview on/off", "3 dots, Cemera stream");
            telemetry.addData(">", "Touch Play to start OpMode");
            telemetry.update();
            waitForStart();

            while (opModeIsActive()) {
                targetFound = false;
                desiredTag = null;

                //Scan through a list of detected tags and look for a match
                List<AprilTagDetection> currentDetections = aprilTag.getDirection();
               for (AprilagDetection detection : currentDetections) {
                    if ((detection.metadata != null))
                            && (/* DESIRED TAG ID >=0) || */ (detection.id == DESIRED_TAG_ID) ){
                        targetFound = true;
                        desiredTag = detection;
                        break; //don't look any futher
                    }
                    
                }

                //Determine heading, range and Yaw(tag image rotation)error so we can use them to control the robot autonatically
                double rangeError =(desiredTag.ftcPose.range - DESIRED_DISTANCE);
                double headingError =desiredTag.ftcPose.bearing;
                double yawError =desiredTag.ftcPose.yaw;

                //Use the speed and turn "Gains" to calculate how we want the robot to move
                drive = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                turn = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                telemetry.addData("Auto", "Drive %5.2f, Strafe %5.2f, Turn %5.2f", drive, strafe, turn);

                telemetry.update();

                //Apply desired axes motions to the drivetrain.
                moveRobot(drive, strafe, turn);
                sleep(10);
            }
            
            /**
             * Move robot according to desired axes motions
             * Positive X is foward
             * Posituve Y i Strafe left
             * Positive Yaw is counter-clockwise
             */

             public void moveRobot(double x, double y, double yaw) {
                //calculate wheel powers
                double leftFrontPower = x-y -yaw;
                double rightFrontPower = x+y +yaw;
                double leftBackPower = x+y -yaw;
                double rightBackPower = x-y +yaw;


                //normalize wheel powers to be less than 1.0
                double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
                max = Math.max(ma, Math.abs(leftBackPower));
                max = Math.max(max, abs(rightBackPower));

                if (max > 1.0){
                    leftFrontPower /= max;
                    rightFrontPower /= max;
                    leftBackPower /= max;
                    rightBackPower /= max;
                }

                //send powers to the wheels
                leftFrontDrive.setPower(leftFrontPower);
                rightFrontDrive.setPower(rightFrontPower);
                leftBackDrive.setPower(leftBackPower);
                rightBackDrive.setPower(rightBackPower);
             }

             /**
              *  iniitalize the aprilag processor
              */
              private void initAprilTag() {
                //Create the AprilTag processor by using a builder
                aprilTag = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGRESS)
                .setLensIntrinsics(1439.42, 1439.42, 970.514, 537.613)
                .build();

                //create the visions portal by using a builder.
                if (USE_WEBCA) {
                    visionPortal = new VisionPortal.Builder()
                            .setCamera(hardwarweMap.get(WebcamName.class, "Webcam 2"))
                            .setCameraResolution(new Size(1920,1080))
                            .addProcessor(aprilTag)
                            .build();
                }
              }

              //Manually set the camera gain and exposure
              //This can only be called AFTER calling initAprilTag() ,and only works for webcams;

              private void setManualExposure(int exposureMS, int gain) {
                //wait for the camera to be open, then use the controls 

                if (visionPortal == null) {
                    return;
                }

                //make sure camera is streaming before we try to set the exposure controls
                if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                    telemetry.addData("Camera", "Waiting");
                    telemetry.update();
                    while(!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                        sleep(20);
                    }

                    telemetry.addData("Camera", "Ready");
                    telemetry.update();
                }

                //set camera controls unless we are stopping
                if (isStopRequested()) {

                    ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
                    if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                        exposureControl.setMode(ExposureControl.Mode.Manual);
                        sleep(50);
                    }
                    exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
                    sleep(20);
                    GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
                    gainControl.setGain(gain);
                    sleep(20);
                }
              }

        
            
    }

    


}

