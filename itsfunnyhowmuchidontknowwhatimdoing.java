package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Move!", group = "drive")
public class itsfunnyhowmuchidontknowwhatimdoing extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the drive class (SampleMecanumDrive is standard for Road Runner)
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(0, 0, 0);
        waitForStart();  // Wait for the start button

        if (isStopRequested()) return;
        //actually do stuff 
        strafeToPoint(38, 108, 0); //first move to right most yellow block
        //collect block
        strafeToPoint(12, 118, 140); // first move to buckit
        // deposit
        strafeToPoint(38, 118, 0); //first move to middle yellow block
        //collect
        strafeToPoint(12, 118, 140); // move to buckit
        // deposit
        strafeToPoint(38, 126, 0); // move to left yellow block
        // collect
        strafeToPoint(12, 118, 140); // move to buckit
        //deposit

    }
    public void strafethatthang(double distance, double angle, double direction, double heading) {
        drive.setPoseEstimate(startPose);
        int factor =0;
        if (direction == 0) {
            factor = 0;
        }
        else if (direction == -1) {
            factor = -1;
        }
        else if (direction == 1) {
            factor = 1;
        }
        else {
            
        }
        double x = factor * distance * Math.sin(Math.toRadians(angle));  // Negative for left strafing, 0 for straight, 1 for right
        double y = distance * Math.cos(Math.toRadians(angle)); 
        Trajectory strafeTrajectory = drive.trajectoryBuilder(startPose)
        .lineToLinearHeading(new Pose2d(x, y, Math.toRadians(heading)))  // Maintain heading (0)
        .build();
        drive.followTrajectory(strafeTrajectory);
    }
    public void strafeToPoint(double x, double y, double heading) {
        // Get the current pose of the robot (current position and orientation)
        Pose2d currentPose = driveSubsystem.getPoseEstimate();

        // Calculate the vector from the current position to the target position
        double deltaX = x - currentPose.getX();
        double deltaY = y - currentPose.getY();

        Trajectory strafeTrajectory = new TrajectoryBuilder(currentPose, driveSubsystem.getConstraints())
            .strafeTo(new Pose2d(x, y, Math.toRadians(heading)))  // Move to the target (strafe)
            .build();
        // Follow the trajectory
        driveSubsystem.followTrajectory(strafeTrajectory);
    }

    
}
