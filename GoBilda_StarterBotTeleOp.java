package gobildastarterbot;
//importing preworkout
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//declarre OpMode
    @TeleOp(name = "GoBilda_StarterBot TeleOp")
    //declare class & OpMode type
    public class GoBilda_StarterBotTeleOp extends LinearOpMode {
        
        @Override
        public  void rupOpMode() throws InteruptedException {
            //declare your motors and servos, make sure it matches these names match in your config file
            DcMotor RDrive = hardwareMap.dcMotor.get("RDrive");
            DcMotor LDrive = hardwareMap.dcMotor.get("LDrive");
            DcMotor Arm0 = hardwareMap.dcMotor.get("Arm0");
            Servo Intake = hardwareMap.servo.get("Intake");
            Servo IntakeAxle = hardwareMap.servo.get("IntakeAxle");

            
            //Reverse motor directions
            LDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            Arm0.setDirection(DcMotorSimple.Direction.REVERSE);

            //Set default position of servos
            Intake.setPosition(0.0);
            IntakeAxle.setPosition(0.0);
            
            waitForStart();
            if (isStopRequested()) return;

            while (opModeIsActive()) {
                double y gamepad1.left_stick_y; 
                //inverting y stick value because the norma direction is just stupid
                //You should have to add the code to fix the gamepad1.right_stick_x inaccruacy sicne were using GoBilda now

                //were not doing mecacnum or holonomic drivetrain rn so you dont need the math.max(Math.Abs(y))
                //set your motor powers

                LDrive.setPower(1);
                RDrive.setPower(1);

                //controlling arm0 motor power(using gamepad)
                if (gamepad1.dpad_up) {
                    //moving arm up
                    arm0.setPower(0.2);
                } else if (gamepad1.dpad_down ) {
                    //moves arm down
                    arm0.setPower(-0.2);
                } else {
                    //dont move arm without a command
                    arm0.setPower(0);
                }

                if (gamepad1.right_stick.y > 0.5 && gamepad1.left_stick.x == 0) {
                    LDrive.setPower(1);
                    RDrive.setPower(1);
                }

                if (gamepad1.right_stick.y < -0.5 && gamepad1.right_stick.x == 0) {
                    LDrive.setPower(-1);
                    RDrive.setPower(-1);
                }
                
                if (gamepad1.right_right_stick.y > 0.5 && gamepad1.left_stick.x > 0.5) {
                    LDrive.setPower(1);
                    RDrive.setPower(0.5;
                }
                
                if (gamepad1.right_stick.y > 0.5 && gamepad1.left_stick.x < -0.5) {
                    LDrive.setPower(1);
                    RDrive.setPower(-0.5);
                }

                //intake controls
                if (gamepad1.x) {
                    Intake.setPosition(1.0);
                    //this turns the intake on
                } else {
                    Intake.setPosition(0.0);
                    //turns intake off and resets the position of the servo
                }
            }

            //intake position in terms of whether ist folded in or out
            if (gamepad1.right_trigger > 0.5) {
                IntakeAxle.setPosition(1.0);
            }

            
            
        }

    }
