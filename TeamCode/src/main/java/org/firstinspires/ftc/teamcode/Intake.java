package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    // --- Declare DC Motors here ---
    private DcMotor actuator = null;
    private DcMotor rotate = null;
    private DcMotor lift = null;

    // --- Declare Servos Here ---
    private Servo frontGripper = null; //Left Servo
    private Servo liftGripper = null; //RightServo
    private Servo liftRotate = null;


    // -- Subsystem Variables ---

    private boolean liftGripperOpen = false;
    private boolean frontGripperOpen = false;

    private int currentActuatorTarget = 0;
    private int currentIntakeTarget = 0;

    private String actuatorTargetDirection = "OUT";
    private String intakeTargetDirection = "UP";

    // Encoder Values used to limit lift from going too high or too low
    //2023 Values, 2 stages, 117rpm motor
    private int actuatorMiddlePosition = 2000;
    private int actuatorInnerLimit = 175;
    private int actuatorOuterLimit = 2500;
    private int liftDownLimit = -60;
    private int liftUpLimit = -11000;

// 😃
    private double frontGripperClosedPosition = .8;
    private double frontGripperOpenPosition = 1;
    private double liftGripperClosedPosition = .9;
    private double liftGripperOpenPosition = .7;
    private double liftRotateInPosition = 0;
    private double liftRotateOutPosition = .5;








    public Intake(HardwareMap h) {

        // --- Motors --- \\\
        actuator = h.get(DcMotor.class, "actuator");
        rotate = h.get(DcMotor.class,"rotate");
        lift = h.get(DcMotor.class,"lift");

        // --- Servos --- \\\

        frontGripper = h.get(Servo.class, "frontGripper");
        liftGripper = h.get(Servo.class, "liftGripper");
        liftRotate = h.get(Servo.class, "liftRotate");

        // --- Reset Encoders --- \\

        actuator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotate.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // --- Start Encoders --- \\

        actuator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    // --- Gripper Methods ---
    //Open and close the gripper
    public void closeFrontGripper() {
        frontGripper.setPosition(frontGripperClosedPosition);
        frontGripperOpen = false;
    }

    public void openFrontGripper() {
        frontGripper.setPosition(frontGripperOpenPosition);
        frontGripperOpen = true;
    }
    public void openLiftGripper() {
        liftGripper.setPosition(liftGripperOpenPosition);
        liftGripperOpen = true;
    }
    public void closeLiftGripper() {
        liftGripper.setPosition(liftGripperClosedPosition);
        liftGripperOpen = false;
    }

    public void liftRotatePlacePosition() {
        liftRotate.setPosition(liftRotateOutPosition);
    }

    public void liftRotateInPosition() {
       liftRotate.setPosition(liftRotateInPosition);
    }

    // --- Intake Rotational Methods --- \\
    public void rotateIntake(double speed) {
        rotate.setPower(speed);
    }

    // --- Lift Methods --- \\
    public void moveLift(double speed) {lift.setPower(speed);}

    // --- Actuator Methods --- \\
    //Move method oly for testing/resetting
    public void moveActuator(double speed) {
        actuator.setPower(speed);
    }
    // --- Targeted Actuator Methods --- \\
    public void moveActuatorIn(double speed) {
        if (actuator.getCurrentPosition() > actuatorInnerLimit) {
            actuator.setPower(speed);
        } else {
            actuator.setPower(0);
        }
    }
    public void moveActuatorOut(double speed) {
        if (actuator.getCurrentPosition() < actuatorOuterLimit) {
            actuator.setPower(speed);
        } else {
            actuator.setPower(0);
        }


    } public void moveLiftUp(double speed) {
        if (lift.getCurrentPosition() < liftUpLimit) {
            lift.setPower(speed);
        } else {
            lift.setPower(0);
        }
    }
    public void moveLiftDown(double speed) {
        if (lift.getCurrentPosition() > liftDownLimit) {
            lift.setPower(speed);
        } else {
            lift.setPower(0);
        }
    }


    public void actuatorFullRetract(){
        currentActuatorTarget = actuatorInnerLimit;
        actuator.setTargetPosition(actuatorInnerLimit);
        actuatorTargetDirection = "IN";
        actuator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void actuatorFullExtend(){
        currentActuatorTarget = actuatorOuterLimit;
        actuator.setTargetPosition(actuatorOuterLimit);
        actuatorTargetDirection = "OUT";
        actuator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void actuatorToMiddle(){
        currentActuatorTarget = actuatorMiddlePosition;
        actuator.setTargetPosition(actuatorMiddlePosition);
        if(actuator.getCurrentPosition() > actuatorMiddlePosition) {
            actuatorTargetDirection = "IN";
        } else {
            actuatorTargetDirection = "OUT";
        }
        actuator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void stopActuator() {
        actuator.setPower(0);
    }

    public void runActuatorWithEncoder(){actuator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}

    public int getActuatorPosition() { return actuator.getCurrentPosition(); }
    public int getLiftPosition() { return lift.getCurrentPosition(); }
    public int getCurrentActuatorTarget() { return currentActuatorTarget; }
    public String getActuatorTargetDirecton() {
        return actuatorTargetDirection;
    }

    public void printIntakeTelemetry(Telemetry t) {

        t.addData("Actuator Encoder Value", actuator.getCurrentPosition());
        t.addData("Lift Encoder Value", lift.getCurrentPosition());
        t.addData("Actuator Target Position", currentActuatorTarget);
        t.addData("Actuator Target Direction", actuatorTargetDirection);


        t.addData("Front Gripper Open",frontGripperOpen);
        t.addData("Lift Gripper Open", liftGripperOpen);
    }

}
