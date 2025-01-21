package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class MecanumDT extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("drive0");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("drive1");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("drive2");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("drive3");
        DcMotor armMotor = hardwareWareMap.dcMotor.get("arm0");

        wrist  = hardwareMap.get(CRServo.class, "serv0");
        intake = hardwareMap.get(CRServo.class, "serv1");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        //setting all the motors to break mode
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Sigmmus Prime Rollout!");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Regulate motor speeds
            double rx = gamepad1.right_stick_x;

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
            if (gamepad1.right_trigger < 0) {
                armMotor.setPower(0.7);
            } else if (gamepad1.left_trigger > 0) {
                armMotor.setPower(-0.7);
            } else {
                armMotor.setPower(0);
            }

            // Control intake servo
            if (gamepad1.y) {
                intake.setPower(-1.0);
            } else if (gamepad1.x) {
                intake.setPower(1.0);
            } else {
                intake.setPower(0.0);
            }

            // Control wrist servo
            if (gamepad1.a) {
                wrist.setPower(0.5);
            } else if (gamepad1.b) {
                wrist.setPower(0.0);
            }
        }
    }