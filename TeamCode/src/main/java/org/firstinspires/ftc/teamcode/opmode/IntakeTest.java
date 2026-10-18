package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/*
 * Intake Test
 *
 * Hardware (Control Hub):
 *   Motor port 2: "intakeMotor"
 *
 * Controls (gamepad 1):
 *   A           = run intake in
 *   B           = run intake out (reverse)
 *   Right trigger = run intake in at variable speed
 *   Dpad up/down  = raise/lower the A/B power by 0.05
 *
 * If A pushes game pieces OUT, change FORWARD to REVERSE below.
 */
@TeleOp(name = "Intake Test", group = "Test")
public class IntakeTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        DcMotor intakeMotor = hardwareMap.tryGet(DcMotor.class, "intakeMotor");
        if (intakeMotor == null) {
            // Show which motors the robot actually loaded so we can see what's wrong
            telemetry.addLine("intakeMotor NOT FOUND. Motors the robot sees:");
            for (String name : hardwareMap.getAllNames(DcMotor.class)) {
                telemetry.addLine("  " + name);
            }
            telemetry.update();
            waitForStart();
            while (opModeIsActive()) {
                idle();
            }
            return;
        }
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        double intakePower = 0.8;
        boolean lastDpadUp = false;
        boolean lastDpadDown = false;

        telemetry.addLine("Ready. A = in, B = out, RT = variable, Dpad = adjust power");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Adjust power once per button press
            if (gamepad1.dpad_up && !lastDpadUp) {
                intakePower = Math.min(1.0, intakePower + 0.05);
            }
            if (gamepad1.dpad_down && !lastDpadDown) {
                intakePower = Math.max(0.0, intakePower - 0.05);
            }
            lastDpadUp = gamepad1.dpad_up;
            lastDpadDown = gamepad1.dpad_down;

            double power;
            if (gamepad1.a) {
                power = intakePower;
            } else if (gamepad1.b) {
                power = -intakePower;
            } else {
                power = gamepad1.right_trigger;
            }
            intakeMotor.setPower(power);

            telemetry.addData("A/B power setting", "%.2f", intakePower);
            telemetry.addData("Motor power", "%.2f", power);
            telemetry.update();
        }

        intakeMotor.setPower(0);
    }
}
