package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp

public class MecanumTeleOp extends LinearOpMode {

    public DcMotor leftDrive = null; // the left drivetrain motor
    public DcMotor rightDrive = null; // the right drivetrain motor
    public DcMotor ViperSlideMotor = null; // the 1 Viper slide motor
    public DcMotor armMotor = null; // the arm motor
    public CRServo intake = null; // the active intake servo
    public CRServo wrist = null; // the wrist servo

    @Override

    public void runOpMode() throws InterruptedException {
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("drive0");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("drive1");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("drive2");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("drive3");
        DcMotor armMotor = hardwareMap.dcMotor.get("arm0");
        DcMotor ViperSlideMotor = hardwareMap.dcMotor.get("VSM0");
        wrist = hardwareMap.get(CRServo.class, "serv0");
        intake = hardwareMap.get(CRServo.class, "serv1");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        ViperSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // setting all the motors to break mode
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ViperSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Sigmmus Prime Rollout!");
        telemetry.update();

        waitForStart();

        if (isStopRequested())
            return;

        while (opModeIsActive()) {

            int armPosition = armMotor.getCurrentPosition();

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Regulate motor speeds
            double rx = gamepad1.right_stick_x;
            double dy = -gamepad2.right_stick_y;
        
            // Denominator is the largest motor power (1)
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            // Control arm motor
            if (dy > 0.2) {
                armMotor.setPower(-0.4);
            } else if (dy < -0.2) {
                armMotor.setPower(0.4);
            } else {
                armMotor.setPower(-0.01);
            }

            if (armPosition < -1970) {
                armMotor.setPower(0.2);
            }

            // Control intake servo
            if (gamepad2.y) {
                intake.setPower(1.0);
            } else if (gamepad2.x) {
                intake.setPower(-1.0);
            } else {
                intake.setPower(0.0);
            }

            if (gamepad2.right_trigger > 0) {
                ViperSlideMotor.setPower(-0.6);
            } else if (gamepad2.left_trigger > 0) {
                ViperSlideMotor.setPower(0.6);
            } else {
                ViperSlideMotor.setPower(0.03);
            }
        }
    }

}