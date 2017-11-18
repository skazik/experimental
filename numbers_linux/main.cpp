#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

enum DiffLevel_t
{
    Lev_VeryBeginer,    // + and - no negative
    Lev_JustProgress,   // + , - and * no negative
    Lev_PartsNoNegative,// all no negative
    Lev_AllIncluded,    // all and negative
    Lev_Iourka,
};

enum MathAction_t
{
    Act_Plus = 0,
    Act_Minus,
    Act_Multiply,
    Act_Divide,
    Act_Max
};

struct TestInfo_t
{
    int num1;
    int num2;
    int num3;
    MathAction_t act1;
    MathAction_t act2;
    int result;
};

#define PRIORITY(_arg) (((_arg == Act_Multiply) || (_arg == Act_Divide)) ? 1:0)

static char ActSimbol[] = "+-*/";
static DiffLevel_t ActLevel = Lev_VeryBeginer;

int calculate(int num1, int &num2, MathAction_t act)
{
    switch(act)
    {
    case Act_Plus:
        return num1 + num2;
    case Act_Minus:
        if ((ActLevel < Lev_AllIncluded) && (num2 > num1))
            num2 = num1?rand()%num1:num1;
        return num1 - num2;
    case Act_Multiply:
        return num1 * num2;
    case Act_Divide:
        num2 = num2 > 0 ? num2 : rand()%9+1;
        return num1 / num2;
    default:
        break;
    }
    return 0;
}
void printout_test(TestInfo_t *info)
{
    printf("%d %c %d %c %d = %d\n", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3, info->result);
}

void printout_quest(TestInfo_t *info)
{
    printf("%d %c %d %c %d = ", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3);
}

void sprint_quest(char *out, TestInfo_t *info)
{
    sprintf(out, "%d %c %d %c %d = ", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3);
}

void generate_test(TestInfo_t *info)
{
    while (info)
    {
        info->num1 = rand()%10;
        info->num2 = rand()%10;
        info->num3 = rand()%10;

        int act_leveling = ActLevel == Lev_VeryBeginer ? Act_Multiply :
                           ActLevel == Lev_JustProgress ? Act_Divide :
                           ActLevel == Lev_PartsNoNegative ? Act_Max : Act_Max;

        info->act1 = (MathAction_t) (rand() % act_leveling);
        info->act2 = (MathAction_t) (rand() % act_leveling);

        if (PRIORITY(info->act1) >= PRIORITY(info->act2))
        {
            info->result = calculate(info->num1, info->num2, info->act1);
            info->result = calculate(info->result, info->num3, info->act2);
        }
        else
        {
            info->result = calculate(info->num2, info->num3, info->act2);
            if (!info->result && info->act1 == Act_Divide)
                continue; // just reshake all
            info->result = calculate(info->num1, info->result, info->act1);
        }
        break;
    }
}

int main(int argc, char *argv[])
{
    (void) argc;
    (void) argv;

    srand(time(NULL));

    TestInfo_t test;

#if DEBUG_GEN
    for (; ActLevel < Lev_Iourka; ActLevel = (DiffLevel_t)((int)ActLevel + 1))
    {
        for (int i = 0; i < 10; i++)
        {
            generate_test(&test);
            printout_test(&test);
        }
        printf("----------------------------------\n");
    }
#endif

    do {
        int result;
        char input[16];

        generate_test(&test);
        printout_quest(&test);

        scanf("%s", input);

        if (strstr(input, "quit"))
        {
            break;
        }
        else if (1 == sscanf(input, "%d", &result))
        {
            if (result == test.result)
                printf("ok, %d\n", test.result);
            else
                printf("oopos... nope, it's %d\n", test.result);
        }
    } while (1);

    return 0;
}
