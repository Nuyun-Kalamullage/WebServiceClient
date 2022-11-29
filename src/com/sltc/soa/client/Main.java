package com.sltc.soa.client;

import com.sltc.soa.client.stub.DemoWS;
import com.sltc.soa.client.stub.DemoWSService;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static String CurrentClientName;

    public static void main(String[] args ) {
        clientTerminal();
    }

    private static void clientTerminal() {

        DemoWSService demoWSService = new DemoWSService();
        DemoWS demoWSPort = demoWSService.getDemoWSPort();
        Scanner sc = new Scanner(System.in);
        System.out.println("==================================================================");
        System.out.println("===============  Welcome to MID BANK - WEB Server ================");
        System.out.println("=================== AA 1711 - NuYuN Pabasara =====================");
        System.out.println("==================================================================\n");
        System.out.println("Enter 'quit' and press enter, any time to quit from the console.");
        System.out.println("If you are a new user please Type create and press enter:)");
        System.out.print("Please Enter Your User ID : ");
        String name;
        String Fname;
        String Lname;
        String nic;
        int password;
        String userAccount;
        float deposit;
        String menuKey = "";
        int hashCode;

        for (name = sc.nextLine(); !name.toLowerCase().equals("quit"); name = sc.nextLine()) { //Get the unique Name ID from user
            int returnValue = -1;
            if (name.toLowerCase().equals("create")) {
                System.out.print("First Name : ");
                Fname = sc.nextLine();
                System.out.print("Last Name : ");
                Lname = sc.nextLine();
                System.out.print("User Name : ");
                name = sc.nextLine();
                while (demoWSPort.checkUserName(name) || name.isEmpty()) {
                    System.out.print("Enter Valid User Name : ");
                    name = sc.nextLine();
                }
                System.out.print("NIC : ");
                nic = sc.nextLine();
                while (demoWSPort.checkUserName(nic) || nic.isEmpty()) {
                    System.out.print("Enter Valid NIC : ");
                    nic = sc.nextLine();
                }
//                System.out.print("Password : ");
//                password = sc.nextLine().hashCode();
                password = consoleFunc().hashCode();
                System.out.print("Enter amount for deposit : ");
                deposit = readInputInt();
                System.out.print("Do you want to proceed the request (y/n) : ");
                String key;
                for (key = sc.nextLine(); !key.toLowerCase().equals("quit"); key = sc.nextLine()) {
                    if (key.toLowerCase().equals("y")) {
                        demoWSPort.addNewClient(Fname, Lname, nic, name, password, deposit);
                        System.out.println("Account Creation Successful");
                        System.out.print("Please Enter Your User ID to login: ");
                        break;

                    } else if (key.toLowerCase().equals("n")) {
                        System.out.println("Account Creation Unsuccessful");
                        System.out.print("Please Enter Your User ID to login: ");
                        break;
                    }
                    System.out.println("Please Enter valid key");
                }
                if (key.equals("quit")) {
                    System.out.println("LogOut from Server");
                }
            } else {
//                System.out.print("Enter the Password : ");
//                hashCode = sc.nextLine().hashCode();
                hashCode = consoleFunc().hashCode();
                returnValue = demoWSPort.authoriseUser(name, hashCode);
                if (returnValue == 1) {
                    System.out.println("");
                    System.out.println("Login Successfully");
                    CurrentClientName = name;
                    returnValue = -1;
                    break;
                } else if (returnValue == -1) {
                    System.out.println("");
                    System.out.println("Incorrect Password, Try Again.");
                    returnValue = -1;

                } else if (returnValue == -2) {
                    System.out.println("");
                    System.out.println("Incorrect Username or Password, Try Again.");
                    returnValue = -1;

                } else {
                    System.out.println("Enter valid User Name. Please Try again");//print Name ID is not unique.
                    System.out.print("Please Enter Your User ID : ");
                }
                System.out.print("Please Enter Your User ID : ");
            }
        }

        while (!menuKey.toLowerCase().equals("quit")) {

            System.out.println("==================================================================");
            System.out.println();
            System.out.println("1.check Balance");
            System.out.println("2.Money Deposit");
            System.out.println("3.Money Withdraw");
            System.out.println("4.Money Transfer");
            System.out.println("<<Type \"quit\" to exit>>");
            System.out.println("Enter the the number do you want to proceed :");
            menuKey = sc.nextLine();

            if (menuKey.toLowerCase().equals("1")) {
                System.out.println("Account No : " + demoWSPort.getAccountNumber(CurrentClientName));
                System.out.println("Balance : " + demoWSPort.getBalance(CurrentClientName));


            } else if (menuKey.toLowerCase().equals("2")) {

                System.out.print("Enter amount for deposit : ");
                Float amount = readInputInt();
//                System.out.print("Enter your Password : ");
                if (demoWSPort.authoriseUser(CurrentClientName, consoleFunc().hashCode()) == 1) {
                    demoWSPort.deposit(amount, CurrentClientName);
                    System.out.println("Deposit Successful:)");
                    System.out.println("Updated balance : Rs:" + demoWSPort.getBalance(CurrentClientName) + "/=");
                } else {
                    System.out.println("Password Incorrect. Please Try again:(");
                }

            } else if (menuKey.toLowerCase(Locale.ROOT).equals("3")) {

                int response = 0;
                System.out.print("Enter amount for withdraw : ");
                Float amount = readInputInt();
//                System.out.print("Enter your Password : ");
                if (demoWSPort.authoriseUser(CurrentClientName, consoleFunc().hashCode()) == 1) {
                    response = demoWSPort.withdraw(amount, CurrentClientName);
                    ;
                    if (response == 1) {
                        System.out.println("Withdraw Successful:)");
                        System.out.println("Updated balance : Rs:" + demoWSPort.getBalance(CurrentClientName) + "/=");
                    } else {
                        System.out.println("Insufficient money:(");
                    }
                } else {
                    System.out.println("Password Incorrect. Please Try again:(");
                }

            } else if (menuKey.toLowerCase(Locale.ROOT).equals("4")) {

                int response = 0;
                System.out.print("Enter amount for Transfer : ");
                Float amount = readInputInt();
                System.out.print("Enter the user Account : ");
                System.out.flush();
                userAccount = sc.nextLine();
                while (!demoWSPort.checkUserAccount(userAccount) || name.isEmpty()) {
                    System.out.print("Enter Valid User Account : ");
                    userAccount = sc.nextLine();
                }
//                System.out.print("Enter your Password : ");
                if (demoWSPort.authoriseUser(CurrentClientName, consoleFunc().hashCode()) == 1) {
                    response = demoWSPort.transfer(CurrentClientName, amount, userAccount);
                    if (response == 1) {
                        System.out.println("Transfer Successful:)");
                        System.out.println("Updated balance : Rs:" + demoWSPort.getBalance(CurrentClientName) + "/=");
                    } else {
                        System.out.println("Insufficient money:(");
                    }
                } else {
                    System.out.println("Password Incorrect. Please Try again:(");
                }
            }
        }
    }
    public static String consoleFunc() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your password: ");
        EraserThread et = new EraserThread();
        Thread mask = new Thread(et);
        mask.start();
        String pass = scan.nextLine();
        System.out.print("\b");
        mask.stop();

        return pass;
    }

    private static float readInputInt() {
        float inputInt = 0;
        boolean numberFound = false;
        Scanner scan = new Scanner( System.in );
        do {
            String inputStr = scan.next();
            try {
                inputInt = Float.parseFloat(inputStr);
                numberFound = true;
            } catch( Exception e ) {
                System.out.println( "Invalid input " + inputStr + ". Please input a number." );
                System.out.print( "Please input The amount: Rs: " );
            }
        } while( !numberFound );
        return inputInt;
    }
}
