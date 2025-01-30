@TeleOp(name="FTC Starter Kit Example Robot (INTO THE DEEP)", group="Robot")
public class GoBildaTest extends LinearOpMode {

    // Declare OpMode members.
    public DcMotor  leftDrive   = null;
    public DcMotor  rightDrive  = null;
    public DcMotor  armMotor    = null; 
    public CRServo  intake      = null; 
    public Servo    wrist       = null;

    // Arm Position Control Variables
    final double ARM_TICKS_PER_DEGREE = 28 * 250047.0 / 4913.0 * 100.0 / 20.0 * 1/360.0; // Encoder ticks per degree

    final double ARM_COLLAPSED_INTO_ROBOT  = 0;
    double armPosition = ARM_COLLAPSED_INTO_ROBOT;  // Starting position is collapsed into the robot

    @Override
    public void runOpMode() {
        // Define and Initialize Motors
        leftDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        armMotor   = hardwareMap.get(DcMotor.class, "left_arm");

        // Set motor directions
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Set motor zero power behavior to brake
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize arm motor
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Use encoder-based control
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset encoder before starting

        // Initialize servos
        intake = hardwareMap.get(CRServo.class, "intake");
        wrist  = hardwareMap.get(Servo.class, "wrist");

        // Set wrist to folded position initially
        wrist.setPosition(0.8333); // WRIST_FOLDED_IN

        // Send telemetry message to signify robot is ready
        telemetry.addLine("Robot Ready.");
        telemetry.update();

        // Wait for start
        waitForStart();

        // Main loop: Run until the driver presses stop
        while (opModeIsActive()) {

            // Control drivetrain with the left and right joysticks
            double forward = -gamepad1.left_stick_y;
            double rotate  = gamepad1.right_stick_x;

            double left  = forward + rotate;
            double right = forward - rotate;

            // Normalize the values so neither exceed +/- 1.0
            double max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0) {
                left /= max;
                right /= max;
            }

            // Set drivetrain motor power
            leftDrive.setPower(left);
            rightDrive.setPower(right);

            // Control the arm's position using left joystick
            // The armPosition is updated based on the joystick input
            if (Math.abs(gamepad1.left_stick_y) > 0.1) {
                armPosition += gamepad1.left_stick_y * 10; // Adjust speed for arm movement
            }

            // Ensure the arm doesn't move past certain limits (optional)
            armPosition = Math.max(ARM_COLLAPSED_INTO_ROBOT, armPosition); // Don't allow the arm to go too far in
            // You can also add an upper limit here if needed (e.g., a max height for the arm)

            // Set the arm's target position and run it to that position
            armMotor.setTargetPosition((int) armPosition);

            // Run the arm motor to the target position
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(0.5);  // You can adjust this power based on your needs

            // Telemetry feedback
            telemetry.addData("Arm Target Position", armMotor.getTargetPosition());
            telemetry.addData("Arm Current Position", armMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
