package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp

public class AMVSMThresholdTest extends LinearOpMode {

    public DcMotor armMotor = null;
    public DcMotor ViperSlideMotor = null;

    @Overide
    public void runOpMode() throws InteruptedException {

        // Map Hardware
        DcMotor armMotor = hardwareMap.dcMotor.get("arm0");
        DcMotor ViperSlideMotor = hardwareMap.dcMotor.get("VSM0");

        // set directions of motors
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        ViperSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // set brake mode
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ViperSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // telemetry update
        telemetry.addLine(
                "Ready to see the Encoder status of the arm motor. Remeber to be accruate and take note of the encoder # tou get.");
        telemetry.update();

        waitForStart();

        if (isStopRequested())
            return;

        while (opModeIsActive()) {
            // get and display arm motor position
            int armPosition = armMotor.getCurrentPosition();
            telemetry.addData("Current Arm Position is ", armPosition);
            telemetry.update();

        }

    }
}
