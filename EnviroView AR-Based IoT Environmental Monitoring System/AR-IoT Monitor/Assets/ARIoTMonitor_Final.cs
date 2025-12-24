// Imported Libraries
using System.Collections;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.Networking;
using Vuforia;
using TMPro;

public class ARIoTMonitor : MonoBehaviour
{
    // UI elements for displaying sensor data
    public TMP_InputField Temp;
    public TMP_InputField Hum;
    public TMP_InputField Level;

    // Reference to the cube object representing water level
    private GameObject cube;

    // Max scale of cube at 100%
    private Vector3 cubeMaxScale = new Vector3(7.140077f, 0.1782501f, 5.348868f);

    void Start()
    {
        // Find UI elements and cube object in the scene
        Temp = GameObject.Find("InputFieldTemp").GetComponent<TMP_InputField>();
        Hum = GameObject.Find("InputFieldHum").GetComponent<TMP_InputField>();
        Level = GameObject.Find("InputFieldLevel").GetComponent<TMP_InputField>();
        cube = GameObject.Find("Cube");

        // Start updating sensor data continuously
        StartCoroutine(UpdateDataLoop());

        // Initial fetch of the data
        GetData_temp();
        GetData_hum();
        GetData_level();
    }

    // Functions to start fetching data for each sensor
    void GetData_temp() => StartCoroutine(GetData_Coroutine1());
    void GetData_hum() => StartCoroutine(GetData_Coroutine2());
    void GetData_level() => StartCoroutine(GetData_Coroutine3());

    // Continuously update sensor data every 1 second
    IEnumerator UpdateDataLoop()
    {
        while (true)
        {
            yield return GetData_Coroutine1();
            yield return GetData_Coroutine2();
            yield return GetData_Coroutine3();

            yield return new WaitForSeconds(1f);
        }
    }

    // Fetch Temperature Data from Blynk
    IEnumerator GetData_Coroutine1()
    {
        string url = "https://blynk.cloud/external/api/get?token=7kiBV0gu6iDtuDJB4YhsvqkjBfPYyOfy&v0";
        using (UnityWebRequest request = UnityWebRequest.Get(url))
        {
            yield return request.SendWebRequest();
            if (request.result != UnityWebRequest.Result.Success)
            {
                Temp.text = request.error; // Display error if failed
            }
            else
            {
                string result = request.downloadHandler.text;
                result = CleanBlynkResponse(result); // Clean up the response
                Temp.text = result; // Display temperature
            }
        }
    }

    // Fetch Humidity Data from Blynk
    IEnumerator GetData_Coroutine2()
    {
        string url = "https://blynk.cloud/external/api/get?token=7kiBV0gu6iDtuDJB4YhsvqkjBfPYyOfy&v1";
        using (UnityWebRequest request = UnityWebRequest.Get(url))
        {
            yield return request.SendWebRequest();
            if (request.result != UnityWebRequest.Result.Success)
            {
                Hum.text = request.error; // Display error if failed
            }
            else
            {
                string result = request.downloadHandler.text;
                result = CleanBlynkResponse(result); // Clean up the response
                Hum.text = result; // Display humidity
            }
        }
    }

    // Fetch Water Level Data from Blynk
    IEnumerator GetData_Coroutine3()
    {
        string url = "https://blynk.cloud/external/api/get?token=7kiBV0gu6iDtuDJB4YhsvqkjBfPYyOfy&v2";
        using (UnityWebRequest request = UnityWebRequest.Get(url))
        {
            yield return request.SendWebRequest();
            if (request.result != UnityWebRequest.Result.Success)
            {
                Level.text = request.error; // Display error if failed
            }
            else
            {
                string result = request.downloadHandler.text;
                result = CleanBlynkResponse(result); // Clean up the response
                Level.text = result; // Display water level

                UpdateCubeScale(result); // Update Cube size based on level
            }
        }
    }

    // Update Cube Scale based on Water Level
    void UpdateCubeScale(string levelString)
    {
        if (cube == null)
        {
            Debug.LogError("Cube not assigned!");
            return;
        }

        if (float.TryParse(levelString, out float levelPercent))
        {
            // Clamp level between 0 and 100 to avoid invalid values
            levelPercent = Mathf.Clamp(levelPercent, 0f, 100f); // Safety check

            // Calculate new scale keeping width and depth constant, only height changes
            float scaleX = cubeMaxScale.x; // Fixed width
            float scaleY = cubeMaxScale.y; // Fixed depth
            float scaleZ = cubeMaxScale.z * (levelPercent / 100f); // Scaled height (Z)

            cube.transform.localScale = new Vector3(scaleX, scaleY, scaleZ);
        }
        else
        {
            Debug.LogError("Failed to parse Water Level: " + levelString);
        }
    }

    // Remove unwanted characters from Blynk API response
    string CleanBlynkResponse(string raw)
    {
        return raw.Replace("[", "").Replace("]", "").Replace("\"", "");
    }
}