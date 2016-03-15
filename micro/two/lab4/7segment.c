#include <stdint.h>

#include <stdbool.h>

#include "inc/hw_memmap.h"

#include "inc/hw_types.h"

#include "driverlib/sysctl.h"

#include "driverlib/gpio.h"


//Using PB3 as the Output Pin


uint8_t ui8PinData=0; //Note: To enable X pin, data to be sent needs to be 2^x in hexadecimal.
int isr_flag = 0;
uint32_t system_delay = 500000;
int frequency = 100000;
uint8_t pin_0 = 0x1, pin_1 = 0x2,pin_2 = 0x4, pin_3 = 0x8, pin_4 = 0x10, pin_5 = 0x20, pin_6 = 0x40, pin_7 = 0x80;

int main(void) {
  SysCtlClockSet(SYSCTL_SYSDIV_5|SYSCTL_USE_PLL|SYSCTL_XTAL_16MHZ|SYSCTL_OSC_MAIN);



  SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOF);
  SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOC);
  SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOB);

  SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOD);
  SysCtlPeripheralEnable(SYSCTL_PERIPH_TIMER0);//1024hz = 15625




  GPIOPinTypeGPIOOutput(GPIO_PORTC_BASE, GPIO_PIN_4 | GPIO_PIN_5 | GPIO_PIN_6 | GPIO_PIN_7);
  GPIOPinTypeGPIOOutput(GPIO_PORTD_BASE, GPIO_PIN_6 | GPIO_PIN_7);
  GPIOPinTypeGPIOOutput(GPIO_PORTF_BASE, GPIO_PIN_4 | GPIO_PIN_3 | GPIO_PIN_2);

  GPIOPinTypeGPIOInput(GPIO_PORTB_BASE, GPIO_PIN_2);


  //Interrupt Enables
  GPIOIntEnable(GPIO_PORTB_BASE, GPIO_INT_PIN_2);
  GPIOIntRegister(GPIO_PORTB_BASE, interrupt_handler);


  GPIOIntTypeSet(GPIO_PORTB_BASE, GPIO_PIN_2, GPIO_FALLING_EDGE);






  ui8PinData = pin_2;

  while(1) {
    //a. Turn off the control signal for both 7-seg
    // b. Send the data to appear in the first 7-seg
    // c. Turn on the first 7-seg control signal
    // d. Delay loop
    // e. Turn off the control signal for both 7-seg
    // f. Send the data to appear in the second 7-seg
    // g. Turn on the second 7-seg control signal
    // h. Delay loop
    // i. Back to step a

    //GPIOPinWrite(GPIO_PORTD_BASE, GPIO_PIN_7, pin_7);

    if(isr_flag)
    {
      isr_flag = 0;

      system_delay -= 50000;
    }
    GPIOPinWrite(GPIO_PORTF_BASE, (GPIO_PIN_3 | GPIO_PIN_2), pin_2 + pin_3);

    //Send Data
    GPIOPinWrite(GPIO_PORTC_BASE, (GPIO_PIN_4 | GPIO_PIN_5 | GPIO_PIN_6 | GPIO_PIN_7), (pin_6 + pin_4 + pin_7) );
    GPIOPinWrite(GPIO_PORTF_BASE, GPIO_PIN_4, pin_4);

    //Turn on first 7-seg control signal
    GPIOPinWrite(GPIO_PORTF_BASE, (GPIO_PIN_3 | GPIO_PIN_2), pin_3);

    //Delay loop
    SysCtlDelay(system_delay);

    //Turn off control signal for both 7-seg
    GPIOPinWrite(GPIO_PORTF_BASE, (GPIO_PIN_3 | GPIO_PIN_2), pin_2 + pin_3);

    //Send Data
    GPIOPinWrite(GPIO_PORTC_BASE, (GPIO_PIN_4 | GPIO_PIN_5 | GPIO_PIN_6 | GPIO_PIN_7), (pin_6 + pin_4 + pin_7));
    GPIOPinWrite(GPIO_PORTF_BASE, GPIO_PIN_4, pin_4);

    //Turn on second 7-seg control signal
    GPIOPinWrite(GPIO_PORTF_BASE, (GPIO_PIN_3 | GPIO_PIN_2), pin_2);

    //Delay loop
    SysCtlDelay(system_delay);


  }






}

void interrupt_handler(void)
{

  GPIOIntClear(GPIO_PORTB_BASE, GPIO_INT_PIN_2);

  if(GPIOPinRead(GPIO_PORTB_BASE, GPIO_PIN_2) == 0x0) {
    isr_flag = 1;

  }
}
